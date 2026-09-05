package ifl.agentbreaker.knowledgemanager.parser;

import ifl.agentbreaker.knowledgemanager.domain.dtos.document.*;
import ifl.agentbreaker.knowledgemanager.exception.KnowledgeManagerBusinessError;
import ifl.agentbreaker.knowledgemanager.exception.ServiceResponseException;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.contentstream.PDFGraphicsStreamEngine;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.graphics.image.PDImage;
import org.apache.pdfbox.pdmodel.graphics.PDXObject;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.PDFTextStripperByArea;
import org.apache.pdfbox.text.TextPosition;
import org.apache.pdfbox.util.Matrix;
import org.springframework.stereotype.Component;
import technology.tabula.*;
import technology.tabula.extractors.SpreadsheetExtractionAlgorithm;

import javax.imageio.ImageIO;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Component
public class PdfDocumentParser implements DocumentParser
{
    // Identify section number and title. group(1)-section number group(2)-section title
    private static final Pattern SECTION_PATTERN =
            Pattern.compile("^\\s*(\\d+(?:\\.\\d+)*)\\s*[\\.、：:]?\\s+(.+?)\\s*$");
    // Identify chinese section number and title. group(1)-section number group(2)-section title
    private static final Pattern CHINESE_SECTION_PATTERN =
            Pattern.compile("^\\s*第([一二三四五六七八九十]+)章\\s+(.+?)\\s*$");
    // Image caption pattern.
    private static final Pattern IMAGE_CAPTION_PATTERN =
            Pattern.compile("^\\s*(?:Figure|Fig\\.?|图)\\s*\\d+\\s*[:：.、-]?\\s*.+$", Pattern.CASE_INSENSITIVE);
    // Table caption pattern.
    private static final Pattern TABLE_CAPTION_PATTERN =
            Pattern.compile("^\\s*(?:Table|Tab\\.?|表)\\s*\\d+\\s*[:：.、-]?\\s*.*$", Pattern.CASE_INSENSITIVE);

    // Caption anchor requires a separator after the number ('Figure 1.'),
    // body references like 'Figure 1 shows' do not anchor a figure region.
    private static final Pattern CAPTION_ANCHOR_PATTERN =
            Pattern.compile("^\\s*(?:Figure|Fig\\.?|图)\\s*\\d+\\s*[.、:：-]", Pattern.CASE_INSENSITIVE);

    // Minimum text blocks each column must contain for a valid two-column layout.
    private static final int MIN_COLUMN_BLOCKS = 3;

    // Minimum gutter width relative to the text span for a valid two-column layout.
    private static final float MIN_GUTTER_RATIO = 0.02f;

    // Minimum vertical overlap between the two columns for a valid two-column layout.
    private static final float MIN_COLUMN_OVERLAP_RATIO = 0.3f;

    // Abstract line opens a separate unnumbered section.
    private static final Pattern ABSTRACT_PATTERN =
            Pattern.compile("^\\s*(?:Abstract\\b|ABSTRACT\\b|摘要)");

    // Maximum section title length for a plausible section header.
    private static final int MAX_SECTION_TITLE_LENGTH = 80;

    // Maximum number of levels in a section number (e.g. 1.1.1 has 3 levels).
    private static final int MAX_SECTION_NUMBER_LEVELS = 4;

    // Tolerance relative to the text span when checking column edge alignment.
    private static final float COLUMN_ALIGN_TOLERANCE_RATIO = 0.06f;

    // Vertical margin (relative to page height) where headers/footers live.
    private static final float HEADER_FOOTER_MARGIN_RATIO = 0.08f;

    // Headers/footers are narrow lines; wide lines in the margin stay (titles).
    private static final float HEADER_FOOTER_MAX_WIDTH_RATIO = 0.5f;

    // Headers/footers are short lines.
    private static final int HEADER_FOOTER_MAX_TEXT_LENGTH = 120;

    // Overlap ratio between a text block and an image region for figure text.
    private static final float FIGURE_TEXT_OVERLAP_RATIO = 0.6f;

    // Overlap ratio between a tabula candidate and an image region to reject it.
    private static final float TABLE_IN_IMAGE_OVERLAP_RATIO = 0.5f;

    @Override
    public ParsedDocument parse(InputStream inputStream, String fileName)
    {
        if (inputStream == null)
        {
            throw new IllegalArgumentException("PDF input stream must not be null.");
        }
        if (fileName == null || fileName.isBlank())
        {
            throw new IllegalArgumentException("PDF file name must not be blank.");
        }

        ParsedDocument parsedDocument = new ParsedDocument();
        // The original file name uploaded by the user.
        parsedDocument.setName(fileName);

        try
        {
            byte[] pdfBytes = inputStream.readAllBytes();

            try (PDDocument pdfDocument = Loader.loadPDF(pdfBytes))
            {
                List<ParsedSection> sections = new ArrayList<>();

                // The currently processing section.
                ParsedSection currentSection = null;

                // Page by page processing of PDF.
                // for (int pageIndex = 0; pageIndex < pdfDocument.getNumberOfPages(); pageIndex++)
                // {
                    // 1.Parse the current page to get text, tables and images.
                    // PageParseResult pageParseResult = parseSinglePage(pdfDocument, pageIndex);
PageParseResult pageParseResult = parseSinglePage(pdfDocument, 0);
                    // 2.Merge section text.
                    for (SectionTextBlock sectionTextBlock : pageParseResult.getSectionTextBlocks())
                    {
                        // Create a new 'ParsedSection'.
                        if (sectionTextBlock.isNewSection())
                        {
                            currentSection = createSection(sectionTextBlock.getSectionNumber());
                            sections.add(currentSection);
                        }
                        // When the main text appears before any section header.
                        if (currentSection == null)
                        {
                            currentSection = createSection(null);
                            sections.add(currentSection);
                        }
                        // Append text content to current section.
                        appendContent(currentSection, sectionTextBlock.getContent());
                    }

                    parsedDocument.getImages()
                                  .addAll(pageParseResult.getParsedImages());
                    parsedDocument.getTables()
                                  .addAll(pageParseResult.getParsedTables());

                // }

                // 3.Set the final parsing result.
                parsedDocument.setSections(sections);
                return parsedDocument;
            }
        }
        catch (IOException e)
        {
            throw new ServiceResponseException(KnowledgeManagerBusinessError.DOCUMENT_PARSING_ERROR);
        }
    }

    /**
     * Extract single page text from PDF by 'pageIndex'.
     *
     * @param pdfDocument
     * @param pageIndex
     * @return
     */
    private String extractPageText(PDDocument pdfDocument, int pageIndex, List<Rectangle> tableRegions, List<ImageRegion> imageRegions) throws IOException
    {
        CoordinateTextStripper stripper = new CoordinateTextStripper();
        stripper.setStartPage(pageIndex + 1);
        stripper.setEndPage(pageIndex + 1);
        stripper.setSortByPosition(true);

        // 1.The plain 'getText()' result is always the primary source of the page text.
        String text = stripper.getText(pdfDocument);

        if (text == null || text.isBlank())
        {
            return text;
        }

        // 2.Coordinates are used for layout analysis only: blocks belonging to
        // headers/footers, figures or tables are dropped from the body text.
        List<TextBlock> textBlocks = stripper.getTextBlocks();
        if (textBlocks.isEmpty())
        {
            return text;
        }

        PDPage page = pdfDocument.getPage(pageIndex);
        float pageWidth = page.getMediaBox().getWidth();
        float pageHeight = page.getMediaBox().getHeight();

        List<TextBlock> bodyBlocks = new ArrayList<>(textBlocks.size());
        boolean filtered = false;
        for (TextBlock block : textBlocks)
        {
            if (isHeaderFooterBlock(block, pageWidth, pageHeight)
                    || isFigureTextBlock(block, imageRegions)
                    || isInsideTable(block, tableRegions))
            {
                filtered = true;
                continue;
            }
            bodyBlocks.add(block);
        }

        if (bodyBlocks.isEmpty())
        {
            return "";
        }

        // 3.Vector figures contain no bitmap image, so their embedded labels
        // are removed by their position above a figure caption instead.
        float columnCut = detectColumnCut(bodyBlocks);
        boolean figureTextRemoved = removeVectorFigureText(bodyBlocks, columnCut);
        if (bodyBlocks.isEmpty())
        {
            return "";
        }

        // 4.Single-column pages without any noise keep the plain 'getText()' result.
        if (columnCut < 0 && !filtered && !figureTextRemoved)
        {
            return text;
        }

        // 5.Otherwise rebuild the reading order line by line: every visual line
        // becomes one region, so two-column lines can never merge again.
        List<List<TextBlock>> readingLines = organizeReadingLines(bodyBlocks, columnCut);
        return extractTextByRegions(page, readingLines);
    }

    /**
     * Headers/footers live in the page margins and are narrow short lines;
     * wide lines (paper titles) inside the top margin are kept.
     */
    private boolean isHeaderFooterBlock(TextBlock block, float pageWidth, float pageHeight)
    {
        boolean inTop = block.maxY() <= pageHeight * HEADER_FOOTER_MARGIN_RATIO;
        boolean inBottom = block.minY() >= pageHeight * (1f - HEADER_FOOTER_MARGIN_RATIO);
        if (!inTop && !inBottom)
        {
            return false;
        }

        float width = block.maxX() - block.minX();
        if (width > pageWidth * HEADER_FOOTER_MAX_WIDTH_RATIO)
        {
            return false;
        }

        String blockText = block.text() == null ? "" : block.text().trim();
        return !blockText.isEmpty() && blockText.length() <= HEADER_FOOTER_MAX_TEXT_LENGTH;
    }

    /**
     * Blocks that mostly sit inside an image region are figure-embedded
     * text (flowchart labels, diagram annotations) and never body text.
     */
    private boolean isFigureTextBlock(TextBlock block, List<ImageRegion> imageRegions)
    {
        if (imageRegions == null || imageRegions.isEmpty())
        {
            return false;
        }

        float blockArea = (block.maxX() - block.minX()) * (block.maxY() - block.minY());
        if (blockArea <= 0)
        {
            return false;
        }

        for (ImageRegion region : imageRegions)
        {
            float x1 = Math.max(block.minX(), region.minX());
            float y1 = Math.max(block.minY(), region.minY());
            float x2 = Math.min(block.maxX(), region.maxX());
            float y2 = Math.min(block.maxY(), region.maxY());

            if (x2 <= x1 || y2 <= y1)
            {
                continue;
            }

            if ((x2 - x1) * (y2 - y1) / blockArea >= FIGURE_TEXT_OVERLAP_RATIO)
            {
                return true;
            }
        }

        return false;
    }

    /**
     * Vector figures (charts drawn as PDF graphics) contain no bitmap image,
     * so their embedded labels are only recognizable by their position above
     * a 'Figure N.' caption. Visual lines directly above a caption are figure
     * text and removed, until a body line (body font size and either aligned
     * to the column edge or at least half the column wide) stops the scan.
     * Returns whether any block was removed.
     */
    private boolean removeVectorFigureText(List<TextBlock> blocks, float columnCut)
    {
        List<TextBlock> captions = new ArrayList<>();
        for (TextBlock block : blocks)
        {
            String blockText = block.text() == null ? "" : block.text().trim();
            if (CAPTION_ANCHOR_PATTERN.matcher(blockText)
                                      .lookingAt())
            {
                captions.add(block);
            }
        }
        if (captions.isEmpty())
        {
            return false;
        }

        float leftBound = Float.MAX_VALUE;
        float rightBound = -Float.MAX_VALUE;
        for (TextBlock block : blocks)
        {
            leftBound = Math.min(leftBound, block.minX());
            rightBound = Math.max(rightBound, block.maxX());
        }

        // Figure labels usually use a smaller font than the body text.
        float bodyFontSize = detectBodyFontSize(blocks);

        Set<TextBlock> figureTextBlocks = new HashSet<>();
        for (TextBlock caption : captions)
        {
            // The column the caption lives in.
            float columnLeft = leftBound;
            float columnRight = rightBound;
            if (columnCut >= 0 && !(caption.minX() < columnCut && caption.maxX() > columnCut))
            {
                if ((caption.minX() + caption.maxX()) / 2f <= columnCut)
                {
                    columnRight = columnCut;
                }
                else
                {
                    columnLeft = columnCut;
                }
            }
            float columnWidth = columnRight - columnLeft;
            if (columnWidth <= 0)
            {
                continue;
            }

            float alignTolerance = columnWidth * 0.03f;

            // Collect the blocks above the caption inside its column.
            List<TextBlock> above = new ArrayList<>();
            for (TextBlock block : blocks)
            {
                if (block == caption || block.maxY() > caption.minY())
                {
                    continue;
                }
                if (block.minX() < columnLeft - alignTolerance || block.maxX() > columnRight + alignTolerance)
                {
                    continue;
                }
                above.add(block);
            }

            // Scan the visual lines above the caption from bottom to top.
            List<List<TextBlock>> lines = groupBlocksIntoLines(above);
            for (int i = lines.size() - 1; i >= 0; i--)
            {
                List<TextBlock> line = lines.get(i);

                // Another caption above means a different figure ends there.
                boolean anotherCaption = false;
                for (TextBlock block : line)
                {
                    String blockText = block.text() == null ? "" : block.text().trim();
                    if (CAPTION_ANCHOR_PATTERN.matcher(blockText)
                                              .lookingAt())
                    {
                        anotherCaption = true;
                        break;
                    }
                }
                if (anotherCaption)
                {
                    break;
                }

                float lineLeft = Float.MAX_VALUE;
                float lineRight = -Float.MAX_VALUE;
                float lineFontSize = 0f;
                for (TextBlock block : line)
                {
                    lineLeft = Math.min(lineLeft, block.minX());
                    lineRight = Math.max(lineRight, block.maxX());
                    lineFontSize = Math.max(lineFontSize, block.fontSize());
                }

                // A body line uses the body font size and either starts at the
                // column edge or fills at least half the column; figure labels
                // are smaller, centered or scattered, so the scan stops there.
                boolean isBodyLine = lineFontSize >= bodyFontSize * 0.9f
                        && (lineLeft <= columnLeft + alignTolerance
                            || lineRight - lineLeft >= columnWidth * 0.5f);
                if (isBodyLine)
                {
                    break;
                }
                figureTextBlocks.addAll(line);
            }
        }

        blocks.removeAll(figureTextBlocks);
        return !figureTextBlocks.isEmpty();
    }

    /**
     * The dominant font size of a page, weighted by text length, so figure
     * labels (small fonts) can be told apart from the running body text.
     */
    private float detectBodyFontSize(List<TextBlock> blocks)
    {
        Map<Float, Integer> weights = new HashMap<>();
        for (TextBlock block : blocks)
        {
            if (block.text() == null || block.text().isBlank())
            {
                continue;
            }
            float rounded = Math.round(block.fontSize() * 2f) / 2f;
            if (rounded <= 0f)
            {
                continue;
            }
            weights.merge(rounded, block.text().length(), Integer::sum);
        }
        return weights.entrySet()
                      .stream()
                      .max(Map.Entry.comparingByValue())
                      .map(Map.Entry::getKey)
                      .orElse(10f);
    }

    /**
     * Detect a two-column layout and return the gutter's x-coordinate,
     * or -1 when the page is single-column.
     * <p>
     * The cut is derived from the actual text distribution instead of a fixed
     * 'pageWidth / 2', so figures or captions placed on the right side do not
     * break the detection, nor do asymmetric column splits.
     */
    private float detectColumnCut(List<TextBlock> textBlocks)
    {
        float leftBound = Float.MAX_VALUE;
        float rightBound = -Float.MAX_VALUE;
        for (TextBlock block : textBlocks)
        {
            leftBound = Math.min(leftBound, block.minX());
            rightBound = Math.max(rightBound, block.maxX());
        }
        float span = rightBound - leftBound;
        if (span <= 0)
        {
            return -1;
        }

        float minGutter = span * MIN_GUTTER_RATIO;
        float tolerance = span * COLUMN_ALIGN_TOLERANCE_RATIO;
        float crossingPenalty = span * 0.5f;
        float bestCut = -1;
        float bestScore = -Float.MAX_VALUE;

        for (float cut = leftBound + span * 0.2f; cut <= leftBound + span * 0.8f; cut += span * 0.01f)
        {
            List<TextBlock> left = new ArrayList<>();
            List<TextBlock> right = new ArrayList<>();

            // Blocks straddling the candidate cut are spanning elements
            // (titles, full-width figures) and join neither column.
            for (TextBlock block : textBlocks)
            {
                if (block.minX() < cut && block.maxX() > cut)
                {
                    continue;
                }
                if ((block.minX() + block.maxX()) / 2f <= cut)
                {
                    left.add(block);
                }
                else
                {
                    right.add(block);
                }
            }

            if (left.size() < MIN_COLUMN_BLOCKS || right.size() < MIN_COLUMN_BLOCKS)
            {
                continue;
            }

            float leftMaxEdge = -Float.MAX_VALUE;
            float rightMinEdge = Float.MAX_VALUE;
            for (TextBlock block : left)
            {
                leftMaxEdge = Math.max(leftMaxEdge, block.maxX());
            }
            for (TextBlock block : right)
            {
                rightMinEdge = Math.min(rightMinEdge, block.minX());
            }

            // A real gutter is an empty vertical strip between the columns.
            float gutter = rightMinEdge - leftMaxEdge;
            if (gutter < minGutter)
            {
                continue;
            }

            // Real columns have aligned inner edges, scattered figure text does not.
            // Only one column has to be well aligned: on a paper's first page the
            // right column is often a figure whose labels never line up, while the
            // left column body text is still perfectly justified.
            int leftAligned = 0;
            for (TextBlock block : left)
            {
                if (leftMaxEdge - block.maxX() <= tolerance)
                {
                    leftAligned++;
                }
            }
            int rightAligned = 0;
            for (TextBlock block : right)
            {
                if (block.minX() - rightMinEdge <= tolerance)
                {
                    rightAligned++;
                }
            }
            float leftAlignedRatio = leftAligned / (float) left.size();
            float rightAlignedRatio = rightAligned / (float) right.size();
            if (leftAlignedRatio < 0.5f && rightAlignedRatio < 0.5f)
            {
                continue;
            }

            // Real side-by-side columns coexist vertically. Stacked content
            // (e.g. a short block above, a figure below, both off-center) does not.
            float leftTop = Float.MAX_VALUE;
            float leftBottom = -Float.MAX_VALUE;
            for (TextBlock block : left)
            {
                leftTop = Math.min(leftTop, block.minY());
                leftBottom = Math.max(leftBottom, block.maxY());
            }
            float rightTop = Float.MAX_VALUE;
            float rightBottom = -Float.MAX_VALUE;
            for (TextBlock block : right)
            {
                rightTop = Math.min(rightTop, block.minY());
                rightBottom = Math.max(rightBottom, block.maxY());
            }
            float overlap = Math.min(leftBottom, rightBottom) - Math.max(leftTop, rightTop);
            float minColumnHeight = Math.min(leftBottom - leftTop, rightBottom - rightTop);
            if (minColumnHeight <= 0 || overlap / minColumnHeight < MIN_COLUMN_OVERLAP_RATIO)
            {
                continue;
            }

            // Straddling elements are penalized but do not invalidate the cut.
            int crossing = 0;
            for (TextBlock block : textBlocks)
            {
                if (block.minX() < cut && block.maxX() > cut)
                {
                    crossing++;
                }
            }
            float score = gutter - crossing * crossingPenalty;
            if (score > bestScore)
            {
                bestScore = score;
                bestCut = cut;
            }
        }

        return bestCut;
    }

    /**
     * Organize body blocks into visual lines in reading order: full-width
     * bands (spanning titles, spanning figures) top-down, and inside each
     * band the left column lines first, then the right column lines.
     */
    private List<List<TextBlock>> organizeReadingLines(List<TextBlock> blocks, float columnCut)
    {
        if (columnCut < 0)
        {
            return groupBlocksIntoLines(blocks);
        }

        List<TextBlock> straddling = new ArrayList<>();
        for (TextBlock block : blocks)
        {
            if (block.minX() < columnCut && block.maxX() > columnCut)
            {
                straddling.add(block);
            }
        }
        straddling.sort(Comparator.comparing(TextBlock::minY).thenComparing(TextBlock::minX));

        // Merge vertically overlapping straddling blocks into full-width bands.
        List<float[]> bands = new ArrayList<>();
        for (TextBlock block : straddling)
        {
            if (!bands.isEmpty() && block.minY() <= bands.get(bands.size() - 1)[1])
            {
                float[] last = bands.get(bands.size() - 1);
                last[1] = Math.max(last[1], block.maxY());
            }
            else
            {
                bands.add(new float[]{block.minY(), block.maxY()});
            }
        }

        float pageTop = Float.MAX_VALUE;
        float pageBottom = -Float.MAX_VALUE;
        for (TextBlock block : blocks)
        {
            pageTop = Math.min(pageTop, block.minY());
            pageBottom = Math.max(pageBottom, block.maxY());
        }

        List<List<TextBlock>> result = new ArrayList<>();
        float cursor = pageTop;
        for (float[] band : bands)
        {
            appendColumnLines(blocks, columnCut, cursor, band[0], result);
            result.addAll(groupBlocksIntoLines(selectBlocksInBand(blocks, band[0], band[1])));
            cursor = band[1];
        }
        appendColumnLines(blocks, columnCut, cursor, pageBottom, result);
        return result;
    }

    /**
     * Append the reading lines of the two columns inside the vertical range
     * [top, bottom): left column lines first, then right column lines.
     */
    private void appendColumnLines(List<TextBlock> blocks, float columnCut, float top, float bottom, List<List<TextBlock>> result)
    {
        if (bottom <= top)
        {
            return;
        }

        List<TextBlock> left = new ArrayList<>();
        List<TextBlock> right = new ArrayList<>();
        for (TextBlock block : blocks)
        {
            float centerY = (block.minY() + block.maxY()) / 2f;
            if (centerY < top || centerY >= bottom)
            {
                continue;
            }
            if (block.minX() < columnCut && block.maxX() > columnCut)
            {
                continue;
            }
            if ((block.minX() + block.maxX()) / 2f <= columnCut)
            {
                left.add(block);
            }
            else
            {
                right.add(block);
            }
        }

        result.addAll(groupBlocksIntoLines(left));
        result.addAll(groupBlocksIntoLines(right));
    }

    private List<TextBlock> selectBlocksInBand(List<TextBlock> blocks, float top, float bottom)
    {
        List<TextBlock> bandBlocks = new ArrayList<>();
        for (TextBlock block : blocks)
        {
            float centerY = (block.minY() + block.maxY()) / 2f;
            if (centerY >= top && centerY <= bottom)
            {
                bandBlocks.add(block);
            }
        }
        return bandBlocks;
    }

    /**
     * Group vertically overlapping blocks into visual lines.
     */
    private List<List<TextBlock>> groupBlocksIntoLines(List<TextBlock> blocks)
    {
        List<List<TextBlock>> lines = new ArrayList<>();
        List<TextBlock> sorted = new ArrayList<>(blocks);
        sorted.sort(Comparator.comparing(TextBlock::minY).thenComparing(TextBlock::minX));

        List<TextBlock> currentLine = null;
        float currentBottom = -Float.MAX_VALUE;
        for (TextBlock block : sorted)
        {
            if (currentLine != null && block.minY() <= currentBottom)
            {
                currentLine.add(block);
                currentBottom = Math.max(currentBottom, block.maxY());
            }
            else
            {
                currentLine = new ArrayList<>();
                currentLine.add(block);
                currentBottom = block.maxY();
                lines.add(currentLine);
            }
        }
        return lines;
    }

    /**
     * Extract the text of every visual line through PDFBox's region stripper,
     * so the line text itself is still generated by PDFBox.
     */
    private String extractTextByRegions(PDPage page, List<List<TextBlock>> readingLines) throws IOException
    {
        PDFTextStripperByArea regionStripper = new PDFTextStripperByArea();
        regionStripper.setSortByPosition(true);

        for (int i = 0; i < readingLines.size(); i++)
        {
            float minX = Float.MAX_VALUE;
            float minY = Float.MAX_VALUE;
            float maxX = -Float.MAX_VALUE;
            float maxY = -Float.MAX_VALUE;
            for (TextBlock block : readingLines.get(i))
            {
                minX = Math.min(minX, block.minX());
                minY = Math.min(minY, block.minY());
                maxX = Math.max(maxX, block.maxX());
                maxY = Math.max(maxY, block.maxY());
            }
            regionStripper.addRegion("line" + i, new Rectangle2D.Float(minX - 1, minY - 1, maxX - minX + 2, maxY - minY + 2));
        }
        regionStripper.extractRegions(page);

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < readingLines.size(); i++)
        {
            String lineText = regionStripper.getTextForRegion("line" + i).trim();
            if (lineText.isEmpty())
            {
                continue;
            }
            if (builder.length() > 0)
            {
                builder.append('\n');
            }
            builder.append(lineText);
        }
        return builder.toString();
    }

    private boolean isInsideTable(TextBlock textBlock, List<Rectangle> tableRegions)
    {
        if (tableRegions == null || tableRegions.isEmpty())
        {
            return false;
        }

        for (Rectangle table : tableRegions)
        {
            if (hasSignificantOverlap(textBlock, table))
            {
                return true;
            }
        }

        return false;
    }

    private boolean hasSignificantOverlap(TextBlock textBlock, Rectangle table)
    {
        float x1 =
                Math.max(
                        textBlock.minX(),
                        table.getLeft());

        float y1 =
                Math.max(
                        textBlock.minY(),
                        table.getTop());

        float x2 =
                Math.min(
                        textBlock.maxX(),
                        table.getRight());

        float y2 =
                Math.min(
                        textBlock.maxY(),
                        table.getBottom());

        if (x2 <= x1 || y2 <= y1)
        {
            return false;
        }

        float intersectionWidth =
                x2 - x1;

        float intersectionHeight =
                y2 - y1;

        float intersectionArea =
                intersectionWidth
                        * intersectionHeight;

        float textArea =
                (textBlock.maxX()
                        - textBlock.minX())
                        *
                        (textBlock.maxY()
                                - textBlock.minY());

        if (textArea <= 0)
        {
            return false;
        }

        double overlapRatio =
                intersectionArea / textArea;

        return overlapRatio >= 0.5;
    }

    /**
     * Parse single page to get all section text, tables and images on the current page.
     *
     * @param pdfDocument
     * @param pageIndex
     * @return
     * @throws IOException
     */
    private PageParseResult parseSinglePage(PDDocument pdfDocument, int pageIndex) throws IOException
    {
        PDPage page = pdfDocument.getPage(pageIndex);

        // 1.Extract image regions for layout analysis.
        List<ImageRegion> imageRegions = extractImageRegions(page);

        // 2.Extract tables while rejecting figure text misdetected by tabula.
        TableExtractionResult tableResult = extractTables(pdfDocument, pageIndex, imageRegions);

        // 3.Extract page text while excluding tables, figures and headers/footers.
        String pageText = extractPageText(pdfDocument, pageIndex, tableResult.tableRegions, imageRegions);

        // 4.Extract images.
        List<ParsedImage> parsedImages = extractImages(page);

        // 5.Extract captions from page text.
        List<CaptionBlock> captions = extractCaptions(pageText);

        // 6.Assign captions to images/tables.
        assignCaptions(parsedImages, tableResult.parsedTables, captions);

        // 7.Parse ordinary section text.
        List<SectionTextBlock> sectionTextBlocks = parseSectionText(pageText, captions);

        PageParseResult result = new PageParseResult();
        result.setSectionTextBlocks(sectionTextBlocks);
        result.setParsedImages(parsedImages);
        result.setParsedTables(tableResult.parsedTables);

        return result;
    }

    private TableExtractionResult extractTables(PDDocument pdfDocument, int pageIndex, List<ImageRegion> imageRegions) throws IOException
    {
        List<ParsedTable> parsedTables = new ArrayList<>();
        List<Rectangle> tableRegions = new ArrayList<>();

        // Tabula's ObjectExtractor shares the caller-owned PDDocument and its
        // close() would close that shared document, so it must never be closed
        // here. The PDDocument itself is managed by the caller's try-with-resources.
        ObjectExtractor objectExtractor = new ObjectExtractor(pdfDocument);

        Page page = objectExtractor.extract(pageIndex + 1);

        List<Table> tables = extractTabulaTables(page, imageRegions);

        for (Table table : tables)
        {
            String markdown = convertTableToMarkdown(table);
            if (markdown.isBlank())
            {
                continue;
            }

            ParsedTable parsedTable = new ParsedTable();
            parsedTable.setContent("");
            parsedTable.setMarkdown(markdown);
            parsedTables.add(parsedTable);

            tableRegions.add(table);
        }

        return new TableExtractionResult(parsedTables, tableRegions);
    }

    private List<Table> extractTabulaTables(Page page, List<ImageRegion> imageRegions)
    {
//        List<Table> tables = new ArrayList<>();
//
//        // 1.Tables with ruling lines.
//        SpreadsheetExtractionAlgorithm spreadsheetAlgorithm = new SpreadsheetExtractionAlgorithm();
//
//        tables.addAll(spreadsheetAlgorithm.extract(page));
//
//        // 2.Borderless tables.
//        BasicExtractionAlgorithm basicAlgorithm = new BasicExtractionAlgorithm();
//        List<Table> basicTables = basicAlgorithm.extract(page);
//
//        // 3.Merge and remove repeated tables.
//        for (Table basicTable : basicTables)
//        {
//            if (!isRepeated(basicTable, tables))
//            {
//                tables.add(basicTable);
//            }
//        }
//
//        return tables;
        SpreadsheetExtractionAlgorithm algorithm =
                new SpreadsheetExtractionAlgorithm();

        return algorithm.extract(page)
                        .stream()
                        .filter(this::isValidTable)
                        .filter(table -> !isInsideImageRegion(table, imageRegions))
                        .toList();
    }

    /**
     * Tabula often mistakes figure-embedded text (flowchart labels, diagram
     * annotations) for a table; candidates that mostly sit inside an image
     * region are rejected.
     */
    private boolean isInsideImageRegion(Table table, List<ImageRegion> imageRegions)
    {
        if (table == null || imageRegions == null || imageRegions.isEmpty())
        {
            return false;
        }

        double tableArea = table.getWidth() * table.getHeight();
        if (tableArea <= 0)
        {
            return false;
        }

        for (ImageRegion region : imageRegions)
        {
            double x1 = Math.max(table.getLeft(), region.minX());
            double y1 = Math.max(table.getTop(), region.minY());
            double x2 = Math.min(table.getRight(), region.maxX());
            double y2 = Math.min(table.getBottom(), region.maxY());

            if (x2 <= x1 || y2 <= y1)
            {
                continue;
            }

            if ((x2 - x1) * (y2 - y1) / tableArea >= TABLE_IN_IMAGE_OVERLAP_RATIO)
            {
                return true;
            }
        }

        return false;
    }

    private boolean isValidTable(Table table)
    {
        if (table == null)
            return false;

        List<List<RectangularTextContainer>> rows =
                table.getRows();

        if (rows.size() < 2)
            return false;

        int columnCount = rows.stream()
                              .mapToInt(List::size)
                              .max()
                              .orElse(0);

        if (columnCount < 2)
            return false;

        // Tabula often mistakes text embedded in a figure (e.g. flowchart labels)
        // for a table, so reject candidates that look like figure text.
        return !isLikelyFigureText(rows);
    }

    /**
     * Heuristic to tell genuine tables apart from figure-embedded labels.
     * <p>
     * Figure labels (e.g. 'Phone call / Open the door / Drink / Watch TV'
     * inside an image) are usually short, numberless natural-language
     * fragments, whereas real data tables tend to contain numeric entries.
     */
    private boolean isLikelyFigureText(List<List<RectangularTextContainer>> rows)
    {
        List<String> cells = new ArrayList<>();
        for (List<RectangularTextContainer> row : rows)
        {
            for (RectangularTextContainer cell : row)
            {
                if (cell.getText() != null && !cell.getText().isBlank())
                {
                    cells.add(cell.getText().trim());
                }
            }
        }

        if (cells.isEmpty())
        {
            return false;
        }

        long numericCells = cells.stream()
                                 .filter(cell -> cell.matches(".*\\d.*"))
                                 .count();
        double numericRatio = numericCells / (double) cells.size();

        int totalLength = cells.stream()
                               .mapToInt(String::length)
                               .sum();
        double avgLength = totalLength / (double) cells.size();

        // Short, numberless natural-language fragments are typical of figure labels.
        return avgLength < 10.0 && numericRatio < 0.15;
    }

    /**
     * Collect the on-page rectangles of all images through PDFBox's
     * graphics stream engine. Only 'drawImage' is of interest here, all
     * other operators are ignored.
     */
    private List<ImageRegion> extractImageRegions(PDPage page) throws IOException
    {
        ImageRegionExtractor extractor = new ImageRegionExtractor(page);
        extractor.processPage(page);
        return extractor.getImageRegions();
    }

    /**
     * The on-page rectangle of one image, in the same top-down coordinate
     * system used by the collected text blocks.
     */
    private record ImageRegion(float minX, float minY, float maxX, float maxY)
    {
    }

    /**
     * Collects the rectangle of every image drawn on the page.
     */
    private static class ImageRegionExtractor extends PDFGraphicsStreamEngine
    {
        private final List<ImageRegion> imageRegions = new ArrayList<>();

        ImageRegionExtractor(PDPage page)
        {
            super(page);
        }

        List<ImageRegion> getImageRegions()
        {
            return imageRegions;
        }

        @Override
        public void drawImage(PDImage pdImage)
        {
            Matrix ctm = getGraphicsState().getCurrentTransformationMatrix();
            Point2D p0 = ctm.transformPoint(0, 0);
            Point2D p1 = ctm.transformPoint(1, 1);

            float minX = (float) Math.min(p0.getX(), p1.getX());
            float maxX = (float) Math.max(p0.getX(), p1.getX());
            float minY = (float) Math.min(p0.getY(), p1.getY());
            float maxY = (float) Math.max(p0.getY(), p1.getY());

            // PDF user space has its origin at the bottom-left corner, while
            // the collected text coordinates grow top-down: flip the y axis.
            float pageHeight = getPage().getMediaBox().getHeight();
            imageRegions.add(new ImageRegion(minX, pageHeight - maxY, maxX, pageHeight - minY));
        }

        @Override
        public void appendRectangle(Point2D p0, Point2D p1, Point2D p2, Point2D p3)
        {
        }

        @Override
        public void clip(int windingRule)
        {
        }

        @Override
        public void moveTo(float x, float y)
        {
        }

        @Override
        public void lineTo(float x, float y)
        {
        }

        @Override
        public void curveTo(float x1, float y1, float x2, float y2, float x3, float y3)
        {
        }

        @Override
        public Point2D getCurrentPoint()
        {
            return null;
        }

        @Override
        public void closePath()
        {
        }

        @Override
        public void endPath()
        {
        }

        @Override
        public void strokePath()
        {
        }

        @Override
        public void fillPath(int windingRule)
        {
        }

        @Override
        public void fillAndStrokePath(int windingRule)
        {
        }

        @Override
        public void shadingFill(COSName shadingName)
        {
        }
    }

    private boolean isRepeated(Table candidateTable, List<Table> existingTables)
    {
        for (Table existing : existingTables)
        {
            if (calculateIoU(candidateTable, existing) > 0.8)
            {
                return true;
            }
        }

        return false;
    }

    private double calculateIoU(Rectangle first, Rectangle second)
    {
        double x1 = Math.max(
                first.getLeft(),
                second.getLeft());

        double y1 = Math.max(
                first.getTop(),
                second.getTop());

        double x2 = Math.min(
                first.getRight(),
                second.getRight());

        double y2 = Math.min(
                first.getBottom(),
                second.getBottom());

        if (x2 <= x1 || y2 <= y1)
        {
            return 0.0;
        }

        double intersection =
                (x2 - x1) * (y2 - y1);

        double firstArea =
                first.getWidth()
                        * first.getHeight();

        double secondArea =
                second.getWidth()
                        * second.getHeight();

        double union =
                firstArea
                        + secondArea
                        - intersection;

        if (union <= 0)
        {
            return 0.0;
        }

        return intersection / union;
    }

    private String convertTableToMarkdown(Table table)
    {
        List<List<RectangularTextContainer>> rows = table.getRows();
        if (rows == null || rows.isEmpty())
        {
            return "";
        }

        StringBuilder markdown = new StringBuilder();
        int columnCount = rows.stream()
                              .mapToInt(List::size)
                              .max()
                              .orElse(0);
        if (columnCount == 0)
        {
            return "";
        }

        // Header.
        List<RectangularTextContainer> header = rows.get(0);
        markdown.append("|");

        for (int columnIndex = 0; columnIndex < columnCount; columnIndex++)
        {
            String cellContent = columnIndex < header.size() ? normalizeCellContent(header.get(columnIndex)
                                                                                          .getText()) : "";
            markdown.append(" ")
                    .append(cellContent)
                    .append(" |");
        }
        markdown.append("\n");

        // Separator.
        markdown.append("|");

        for (int columnIndex = 0; columnIndex < columnCount; columnIndex++)
        {
            markdown.append(" --- |");
        }
        markdown.append("\n");

        // Data rows.
        for (int rowIndex = 1; rowIndex < rows.size(); rowIndex++)
        {
            List<RectangularTextContainer> row = rows.get(rowIndex);
            markdown.append("|");
            for (int columnIndex = 0; columnIndex < columnCount; columnIndex++)
            {
                String cellContent = columnIndex < row.size() ? normalizeCellContent(row.get(columnIndex)
                                                                                        .getText()) : "";
                markdown.append(" ")
                        .append(cellContent)
                        .append(" |");
            }
            markdown.append("\n");
        }

        return markdown.toString()
                       .trim();
    }

    private String normalizeCellContent(String content)
    {
        if (content == null)
        {
            return "";
        }

        return content.replace("|", "\\|")
                      .replaceAll("\\s+", " ")
                      .trim();
    }

    private List<SectionTextBlock> parseSectionText(String pageText, List<CaptionBlock> captions)
    {
        List<SectionTextBlock> sectionTextBlocks = new ArrayList<>();

        if (pageText == null || pageText.isBlank())
        {
            return sectionTextBlocks;
        }

        String[] lines = pageText.split("\\R");

        String currentSectionNumber = null;
        StringBuilder currentContent = new StringBuilder();

        for (int i = 0; i < lines.length; i++)
        {
            String rawLine = lines[i];
            String line = rawLine == null ? "" : rawLine.trim();

            // 1.Skip blank lines and all lines belonging to captions.
            if (line.isBlank() || isCaptionLine(i, captions))
            {
                continue;
            }

            // 2.Detect section header.
            SectionHeader sectionHeader = detectSectionHeader(line);

            if (sectionHeader != null)
            {
                // Save the text belonging to the previous section.
                if (!currentContent.isEmpty())
                {
                    sectionTextBlocks.add(new SectionTextBlock(currentSectionNumber, currentContent.toString(), false));
                    currentContent.setLength(0);
                }

                // A new section starts.
                currentSectionNumber = sectionHeader.getSectionNumber();
                sectionTextBlocks.add(new SectionTextBlock(currentSectionNumber, sectionHeader.getTitle(), true));
                continue;
            }

            // 3.Normal text.
            if (!currentContent.isEmpty())
            {
                currentContent.append("\n");
            }
            currentContent.append(line);
        }

        // 4.Save the remaining text.
        if (!currentContent.isEmpty())
        {
            sectionTextBlocks.add(new SectionTextBlock(currentSectionNumber, currentContent.toString(), false));
        }

        return sectionTextBlocks;
    }

    private boolean isCaptionLine(int lineIndex, List<CaptionBlock> captions)
    {
        if (captions == null || captions.isEmpty())
        {
            return false;
        }

        return captions.stream()
                       .anyMatch(caption -> lineIndex >= caption.getStartLine() && lineIndex <= caption.getEndLine());
    }

    private void assignCaptions(List<ParsedImage> parsedImages, List<ParsedTable> parsedTables, List<CaptionBlock> captions)
    {
        int imageIndex = 0;
        int tableIndex = 0;

        for (CaptionBlock caption : captions)
        {
            if (caption.getCaptionType() == CaptionType.IMAGE)
            {
                if (imageIndex >= parsedImages.size())
                {
                    continue;
                }

                parsedImages.get(imageIndex)
                            .setDescription(caption.getContent());
                imageIndex++;
            }
            else if (caption.getCaptionType() == CaptionType.TABLE)
            {
                if (tableIndex >= parsedTables.size())
                {
                    continue;
                }

                parsedTables.get(tableIndex)
                            .setContent(caption.getContent());
                tableIndex++;
            }
        }
    }

    /**
     * Extract image and table captions from the page text.
     *
     * @param pageText
     * @return
     */
    private List<CaptionBlock> extractCaptions(String pageText)
    {
        List<CaptionBlock> captions = new ArrayList<>();
        if (pageText == null || pageText.isBlank())
        {
            return captions;
        }

        String[] lines = pageText.split("\\R");

        StringBuilder currentCaption = null;
        CaptionType currentType = null;
        int currentStartLine = -1;

        for (int i = 0; i < lines.length; i++)
        {
            String rawLine = lines[i];
            String line = rawLine == null ? "" : rawLine.trim();

            // 1.Caption ends or a blank line.
            if (line.isBlank())
            {
                // A complete caption.
                if (currentCaption != null)
                {
                    captions.add(createCaptionBlock(currentType, currentCaption.toString(), currentStartLine, i - 1));
                    currentType = null;
                    currentCaption = null;
                    currentStartLine = -1;
                }
                continue;
            }

            // 2.Image caption starts.
            if (IMAGE_CAPTION_PATTERN.matcher(line)
                                     .matches())
            {
                // Ensure that the previous caption has ended.
                if (currentCaption != null)
                {
                    captions.add(createCaptionBlock(currentType, currentCaption.toString(), currentStartLine, i - 1));
                }

                // New image caption.
                currentType = CaptionType.IMAGE;
                currentCaption = new StringBuilder(line);
                currentStartLine = i;
                continue;
            }

            // 3.Table caption starts.
            if (TABLE_CAPTION_PATTERN.matcher(line)
                                     .matches())
            {
                // Ensure that the previous caption has ended.
                if (currentCaption != null)
                {
                    captions.add(createCaptionBlock(currentType, currentCaption.toString(), currentStartLine, i - 1));
                }

                // New table caption.
                currentType = CaptionType.TABLE;
                currentCaption = new StringBuilder(line);
                currentStartLine = i;
                continue;
            }

            // 4.A caption is currently being parsed.
            if (currentCaption != null)
            {
                currentCaption.append("\n" + line);
            }
        }

        // Finish the caption at the end of the page.
        if (currentCaption != null)
        {
            captions.add(createCaptionBlock(currentType, currentCaption.toString(), currentStartLine, lines.length - 1));
        }

        return captions;
    }

    /**
     * Create a caption block.
     *
     * @param currentType
     * @param content
     * @param startLine
     * @param endLine
     * @return
     */
    private CaptionBlock createCaptionBlock(CaptionType currentType, String content, int startLine, int endLine)
    {
        CaptionBlock caption = new CaptionBlock();
        caption.setCaptionType(currentType);
        caption.setContent(content);
        caption.setStartLine(startLine);
        caption.setEndLine(endLine);
        return caption;
    }

    /**
     * Detect the section header.
     *
     * @param line
     * @return
     */
    private SectionHeader detectSectionHeader(String line)
    {
        // 1.'Abstract' opens a separate unnumbered section, so title-page
        // content and the abstract body never merge into one section.
        if (ABSTRACT_PATTERN.matcher(line)
                             .lookingAt())
        {
            return new SectionHeader(null, line.trim());
        }

        // 2.The number section header.(e.g. 1 Introduction/1.1 Background)
        Matcher matcher = SECTION_PATTERN.matcher(line);
        if (matcher.matches())
        {
            String number = matcher.group(1);
            String title = matcher.group(2);

            // A number at the start of a line alone does not make a header:
            // year-like numbers ('2021 IEEE ...') and sentence fragments
            // ('2 seconds 1 min ...') must be rejected.
            if (isPlausibleSectionNumber(number) && isPlausibleSectionTitle(title))
            {
                return new SectionHeader(number, title);
            }

            return null;
        }

        // 3.The chinese number section header.(e.g. 第一章 绪论)
        Matcher chineseMatcher = CHINESE_SECTION_PATTERN.matcher(line);
        if (chineseMatcher.matches())
        {
            String number = String.valueOf(chineseNumberToInt(chineseMatcher.group(1)));
            String title = chineseMatcher.group(2);
            if (isPlausibleSectionTitle(title))
            {
                return new SectionHeader(number, title);
            }

            return null;
        }

        // 4.Other lines are treated as normal text.
        return null;
    }

    /**
     * Check that a section number looks like a real chapter number rather
     * than a year, an ISBN fragment or a deep enumeration.
     */
    private boolean isPlausibleSectionNumber(String number)
    {
        if (number == null || number.isBlank())
        {
            return false;
        }

        String[] levels = number.split("\\.");

        // Real chapters never reach three digits ('2021', '978' are years/ISBNs).
        if (levels[0].length() > 2)
        {
            return false;
        }

        // '1.1.1.1.1' is not a section number used in papers.
        return levels.length <= MAX_SECTION_NUMBER_LEVELS;
    }

    /**
     * Check that a title looks like a section title rather than body text.
     */
    private boolean isPlausibleSectionTitle(String title)
    {
        if (title == null || title.isBlank())
        {
            return false;
        }

        String trimmed = title.trim();
        if (trimmed.length() > MAX_SECTION_TITLE_LENGTH)
        {
            return false;
        }

        // English section titles start with an uppercase word; body fragments
        // like 'seconds 1 min' start lowercase. Chinese titles are unaffected.
        char first = trimmed.charAt(0);
        return first < 'a' || first > 'z';
    }

    private int chineseNumberToInt(String chineseNumber)
    {
        Map<Character, Integer> digitMap = Map.of(
                '一', 1,
                '二', 2,
                '三', 3,
                '四', 4,
                '五', 5,
                '六', 6,
                '七', 7,
                '八', 8,
                '九', 9
        );

        // Case 1: Containing '十'.
        if (chineseNumber.contains("十"))
        {
            int indexOfTen = chineseNumber.indexOf('十');
            int tens = 1;
            int ones = 0;

            // There are characters before '十'.
            if (indexOfTen > 0)
            {
                tens = digitMap.getOrDefault(chineseNumber.charAt(indexOfTen - 1), 1);
            }
            // There are characters after '十'.
            if (indexOfTen < chineseNumber.length() - 1)
            {
                ones = digitMap.getOrDefault(chineseNumber.charAt(indexOfTen + 1), 0);
            }
            return tens * 10 + ones;
        }
        // Case 2: No containing "十"
        return digitMap.getOrDefault(chineseNumber.charAt(0), 0);
    }

    /**
     * Extract all images from one PDF page.
     *
     * @param page
     * @return
     * @throws IOException
     */
    private List<ParsedImage> extractImages(PDPage page) throws IOException
    {
        List<ParsedImage> parsedImages = new ArrayList<>();

        // Access all resources accessible from this page.
        PDResources resources = page.getResources();
        if (resources == null)
        {
            return parsedImages;
        }

        // Traverse all XObject names on the current page.
        for (COSName xObjectName : resources.getXObjectNames())
        {
            // Get the corresponding XObject base on its name.
            PDXObject xObject = resources.getXObject(xObjectName);

            // XObject may not be an image.
            if (!(xObject instanceof PDImageXObject image))
            {
                continue;
            }

            // Get the 'BufferedImage' from the 'PDImageXObject'.
            BufferedImage bufferedImage = image.getImage();
            if (bufferedImage == null)
            {
                continue;
            }

            // Convert the image to PNG binary data.
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "png", output);
            byte[] imageData = output.toByteArray();
            if (imageData.length == 0)
            {
                continue;
            }

            ParsedImage parsedImage = new ParsedImage();
            parsedImage.setData(imageData);
            parsedImage.setWidth(bufferedImage.getWidth());
            parsedImage.setHeight(bufferedImage.getHeight());
            parsedImage.setDescription(null);
            parsedImage.setNameInOss(null);
            parsedImages.add(parsedImage);
        }

        return parsedImages;
    }

    /**
     * Create a 'ParsedSection'.
     *
     * @param sectionNumber
     * @return
     */
    private ParsedSection createSection(String sectionNumber)
    {
        ParsedSection parsedSection = new ParsedSection();
        parsedSection.setSectionNumber(sectionNumber);
        parsedSection.setContent("");
        return parsedSection;
    }

    /**
     * Append section content.
     *
     * @param parsedSection
     * @param content
     */
    private void appendContent(ParsedSection parsedSection, String content)
    {
        if (parsedSection == null || content == null || content.isBlank())
        {
            return;
        }

        String current = parsedSection.getContent();
        if (current == null || current.isBlank())
        {
            parsedSection.setContent(content.trim());
            return;
        }
        parsedSection.setContent(current.trim() + "\n" + content.trim());
    }

    private record TextBlock(
            String text,
            float minX,
            float minY,
            float maxX,
            float maxY,
            float fontSize)
    {
    }

    private record TableExtractionResult(
            List<ParsedTable> parsedTables,
            List<Rectangle> tableRegions)
    {
    }

    private static class CoordinateTextStripper extends PDFTextStripper
    {
        private final List<TextBlock> textBlocks =
                new ArrayList<>();

        CoordinateTextStripper() throws IOException
        {
            setSortByPosition(true);
        }

        List<TextBlock> getTextBlocks()
        {
            return textBlocks;
        }

        @Override
        protected void writeString(
                String text,
                List<TextPosition> textPositions)
                throws IOException
        {
            if (textPositions == null
                    || textPositions.isEmpty())
            {
                return;
            }

            float minX = Float.MAX_VALUE;
            float minY = Float.MAX_VALUE;
            float maxX = -Float.MAX_VALUE;
            float maxY = -Float.MAX_VALUE;
            float fontSize = 0f;

            for (TextPosition position : textPositions)
            {
                float x = position.getXDirAdj();
                float y = position.getYDirAdj();
                float width = position.getWidthDirAdj();
                float height = position.getHeightDir();

                minX = Math.min(minX, x);
                minY = Math.min(minY, y);

                maxX = Math.max(maxX, x + width);
                maxY = Math.max(maxY, y + height);
                fontSize = Math.max(fontSize, height);
            }

            textBlocks.add(
                    new TextBlock(
                            text,
                            minX,
                            minY,
                            maxX,
                            maxY,
                            fontSize));

            super.writeString(text, textPositions);
        }
    }
}
