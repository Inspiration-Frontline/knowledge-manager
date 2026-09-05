package ifl.agentbreaker.knowledgemanager.parser;

import ifl.agentbreaker.knowledgemanager.domain.dtos.document.ParsedDocument;
import org.springframework.stereotype.Component;

import java.io.InputStream;

@Component
public class MarkdownDocumentParser implements DocumentParser
{
    @Override
    public ParsedDocument parse(InputStream inputStream, String fileName)
    {
        ParsedDocument parsedDocument = new ParsedDocument();

        parsedDocument.setName(fileName);

        /*
         * Markdown:
         *
         * # 一级标题
         *
         * ## 二级标题
         *
         * 图片:
         *
         * ![](xxx)
         */

        return parsedDocument;
    }
}
