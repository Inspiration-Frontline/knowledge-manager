package ifl.agentbreaker.knowledgemanager.domain.dtos.document;

import lombok.Data;

import java.util.List;

@Data
public class ParsedSection
{
    /**
     * section编号
     * 统一格式:
     * 1
     * 1.1
     * 1.1.1
     * 无法解析:
     * null
     * 摘要:
     * Abstract
     */
    private String sectionNumber;

    /**
     * section正文
     */
    private String content;
}