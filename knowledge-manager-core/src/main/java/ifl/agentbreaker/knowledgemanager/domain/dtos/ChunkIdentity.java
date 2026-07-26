package ifl.agentbreaker.knowledgemanager.domain.dtos;

import ifl.agentbreaker.knowledgemanager.domain.constants.ChunkType;
import lombok.Data;

// TODO: Mention Dino to review if we need this table. （还是删掉吧）
@Data
public class ChunkIdentity
{
    /**
     * chunk来源类别
     */
    private ChunkType chunkType;

    /**
     * chunk来源类别的ID（比如ID为0的document或ID为26的video）
     */
    private long chunkTypeId;
}
