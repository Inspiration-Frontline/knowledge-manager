package ifl.agentbreaker.knowledgemanager.parser;

import ifl.agentbreaker.knowledgemanager.domain.dtos.document.ParsedDocument;
import org.springframework.stereotype.Component;

import java.io.InputStream;

@Component
public class HtmlDocumentParser implements DocumentParser
{
    @Override
    public ParsedDocument parse(InputStream inputStream, String fileName)
    {
        ParsedDocument parsedDocument = new ParsedDocument();

        parsedDocument.setName(fileName);

        /*
         * HTML:
         *
         * Jsoup解析
         *
         * body文本
         *
         * img标签
         *
         * alt/caption
         */

        return parsedDocument;
    }
}
