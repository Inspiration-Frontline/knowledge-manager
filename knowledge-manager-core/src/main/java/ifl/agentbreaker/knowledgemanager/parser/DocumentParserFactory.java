package ifl.agentbreaker.knowledgemanager.parser;

import ifl.agentbreaker.knowledgemanager.domain.constants.DocumentType;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.Map;

@Component
public class DocumentParserFactory
{
    private final Map<DocumentType, DocumentParser> documentParserMap = new EnumMap<>(DocumentType.class);

    public DocumentParserFactory(PdfDocumentParser pdfDocumentParser,
                                 HtmlDocumentParser htmlDocumentParser,
                                 MarkdownDocumentParser markdownDocumentParser,
                                 DocxDocumentParser docxDocumentParser)
    {
        documentParserMap.put(DocumentType.PDF, pdfDocumentParser);
        documentParserMap.put(DocumentType.HTML, htmlDocumentParser);
        documentParserMap.put(DocumentType.MARKDOWN, markdownDocumentParser);
        documentParserMap.put(DocumentType.DOCX, docxDocumentParser);
    }

    /**
     * 根据文档类型获取解析器
     * @param type
     * @return
     */
    public DocumentParser getDocumentParser(DocumentType type)
    {
        DocumentParser documentParser = documentParserMap.get(type);
        return documentParser;
    }
}
