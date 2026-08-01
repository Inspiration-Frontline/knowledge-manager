package ifl.agentbreaker.knowledgemanager.domain.dtos.requests;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class PageImageChunksRequest extends PageRequest
{
    /**
     * 图片知识库ID
     */
    private long knowledgeBaseId;

    /**
     * 关键字 - 模糊查询chunkAbstract
     */
    private String keyword;
}
