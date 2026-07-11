package ifl.agentbreaker.knowledgemanager.domain.dtos.requests;

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

    private List<ChunkIdentity> chunkIdentities;

    /**
     * 关键字（Chunk内容）
     */
    private String keyword;

}
