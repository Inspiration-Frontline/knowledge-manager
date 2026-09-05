package ifl.agentbreaker;

import ifl.agentbreaker.knowledgemanager.domain.dtos.document.ParsedDocument;
import ifl.agentbreaker.knowledgemanager.domain.dtos.document.ParsedImage;
import ifl.agentbreaker.knowledgemanager.domain.dtos.document.ParsedSection;
import ifl.agentbreaker.knowledgemanager.domain.dtos.document.ParsedTable;
import ifl.agentbreaker.knowledgemanager.parser.PdfDocumentParser;
import org.junit.jupiter.api.Test;

import java.io.InputStream;

public class PdfDocumentParserTest
{
    @Test
    void testParse() throws Exception
    {
        PdfDocumentParser parser = new PdfDocumentParser();

        try (InputStream inputStream =
                     getClass().getResourceAsStream("/123.pdf"))
        {
            if (inputStream == null)
            {
                throw new IllegalStateException("123.pdf not found.");
            }

            ParsedDocument document =
                    parser.parse(inputStream, "123.pdf");

            // 1. Basic information.
            System.out.println("========== Document ==========");
            System.out.println("Name: " + document.getName());

            // 2. Sections.
            System.out.println("\n========== Sections ==========");

            for (ParsedSection section : document.getSections())
            {
                System.out.println("--------------------------------");
                System.out.println("Section Number: "
                        + section.getSectionNumber());
                System.out.println("Content:");
                System.out.println(section.getContent());
            }

            // 3. Tables.
            System.out.println("\n========== Tables ==========");

            for (ParsedTable table : document.getTables())
            {
                System.out.println("--------------------------------");
                System.out.println("Caption:");
                System.out.println(table.getContent());

                System.out.println("Markdown:");
                System.out.println(table.getMarkdown());
            }

            // 4. Images.
            System.out.println("\n========== Images ==========");

            int imageIndex = 1;

            for (ParsedImage image : document.getImages())
            {
                System.out.println("--------------------------------");
                System.out.println("Image: " + imageIndex++);
                System.out.println("Width: " + image.getWidth());
                System.out.println("Height: " + image.getHeight());
                System.out.println("Description: "
                        + image.getDescription());
                System.out.println("Data size: "
                        + image.getData().length);
            }
        }
    }
}