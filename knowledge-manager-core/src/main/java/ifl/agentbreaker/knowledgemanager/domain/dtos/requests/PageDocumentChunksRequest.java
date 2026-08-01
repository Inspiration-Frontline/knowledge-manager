package ifl.agentbreaker.knowledgemanager.domain.dtos.requests;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class PageDocumentChunksRequest extends PageRequest
{
    /**
     * 文档知识库ID
     */
    private long knowledgeBaseId;

    /**
     * 关键字
     */
    private String keyword;
}
