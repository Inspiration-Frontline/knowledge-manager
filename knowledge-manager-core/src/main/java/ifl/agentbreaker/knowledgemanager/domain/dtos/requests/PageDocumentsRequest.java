package ifl.agentbreaker.knowledgemanager.domain.dtos.requests;

import ifl.agentbreaker.knowledgemanager.domain.constants.DocumentType;
import ifl.agentbreaker.knowledgemanager.domain.constants.ParsingStatus;
import ifl.agentbreaker.knowledgemanager.domain.constants.SourceType;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class PageDocumentsRequest extends PageRequest
{
    /**
     * 知识库ID
     */
    private long knowledgeBaseId;

    /**
     * 文档名称（模糊查询）
     */
    private String keyword;

    /**
     * 文档类型
     */
    private DocumentType type;

    /**
     * 来源类型
     */
    private SourceType sourceType;

    /**
     * 解析状态
     */
    private ParsingStatus parsingStatus;
}
