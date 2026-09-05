package ifl.agentbreaker.knowledgemanager.parser;

import ifl.agentbreaker.knowledgemanager.domain.dtos.document.ParsedDocument;

import java.io.InputStream;

public interface DocumentParser
{
    /**
     * 解析文档
     * @param inputStream 文件流
     * @param fileName 原始文件名
     * @return ParsedDocument - 解析后的文档
     */
    ParsedDocument parse(InputStream inputStream, String fileName);
}
