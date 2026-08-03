package ifl.agentbreaker.knowledgemanager.domain.dtos.requests;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class PageDocumentImageChunksRequest extends PageRequest
{
    /**
     * 文档图片知识库ID
     */
    private long knowledgeBaseId;

    /**
     * 关键字 - 模糊查询chunkAbstract
     */
    private String keyword;
}
