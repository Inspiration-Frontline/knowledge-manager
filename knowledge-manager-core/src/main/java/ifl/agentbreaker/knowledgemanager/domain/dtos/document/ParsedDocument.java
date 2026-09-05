package ifl.agentbreaker.knowledgemanager.domain.dtos.document;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ParsedDocument
{
    /**
     * 原始文档ID
     */
    private long documentId;

    /**
     * 文档名称
     * 对应Document.name
     */
    private String name;

    /**
     * 文档章节
     */
    private List<ParsedSection> sections = new ArrayList<>();

    /**
     * 文档图片
     */
    private List<ParsedImage> images = new ArrayList<>();

    /**
     * 文档表格
     */
    private List<ParsedTable> tables = new ArrayList<>();
}