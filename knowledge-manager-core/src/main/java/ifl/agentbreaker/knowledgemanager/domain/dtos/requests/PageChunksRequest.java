package ifl.agentbreaker.knowledgemanager.domain.dtos.requests;

import ifl.agentbreaker.knowledgemanager.domain.dtos.ChunkIdentity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class PageChunksRequest extends PageRequest
{

    /**
     * 所属知识库
     */
    private List<Long> knowledgeBaseIds;

    /**
     * chunk来源实体
     */
    private List<ChunkIdentity> chunkIdentities;

    /**
     * 关键字（chunk内容）
     */
    private String keyword;

}
