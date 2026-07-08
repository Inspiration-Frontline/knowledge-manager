package ifl.agentbreaker.knowledgemanager.domain.dtos.requests;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class PageChunksRequest extends PageRequest
{

    /**
     * 所属知识库
     */
    private Long knowledgeBaseId;

    /**
     * 来源文档
     */
    private Long documentId;

    /**
     * 来源视频
     */
    private Long videoId;

    /**
     * 关键字（Chunk内容）
     */
    private String keyword;

}
