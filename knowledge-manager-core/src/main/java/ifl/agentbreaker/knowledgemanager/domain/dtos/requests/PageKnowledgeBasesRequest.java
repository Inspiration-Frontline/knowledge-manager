package ifl.agentbreaker.knowledgemanager.domain.dtos.requests;

import ifl.agentbreaker.knowledgemanager.domain.constants.ChunkType;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class PageKnowledgeBasesRequest extends PageRequest
{
    /**
     * 所属业务ID
     */
    private Long bizId;

    /**
     * 关键字 - 知识库名称
     */
    private String keyword;

    /**
     * 知识库chunk类型 - 文档 or 文档图片 or 图片 or 视频
     */
    private ChunkType chunkType;

    /**
     * 状态
     */
    private Boolean enabled;
}
