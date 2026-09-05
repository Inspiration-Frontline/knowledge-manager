package ifl.agentbreaker.knowledgemanager.parser;

import ifl.agentbreaker.knowledgemanager.domain.dtos.document.ParsedDocument;
import org.springframework.stereotype.Component;

import java.io.InputStream;

@Component
public class DocxDocumentParser implements DocumentParser
{
    @Override
    public ParsedDocument parse(InputStream inputStream, String fileName)
    {
        ParsedDocument parsedDocument = new ParsedDocument();

        parsedDocument.setName(fileName);

        /*
         *
         * Apache POI:
         *
         * Paragraph
         *
         * Picture
         *
         */

        return parsedDocument;
    }
}
