package ifl.agentbreaker.knowledgemanager.domain.dtos.responses;

import ifl.agentbreaker.knowledgemanager.domain.dtos.ChunkIdentity;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ChunkSearchResponse
{

    /**
     * Chunk ID
     */
    private long chunkId;

    /**
     * 知识库ID
     */
    private long knowledgeBaseId;

    /**
     * 相似度分数
     */
    private BigDecimal score;

    /**
     * chunk内容
     */
    private String chunkContent;

    /**
     * chunk来源实体
     */
    private ChunkIdentity chunkIdentity;

    /**
     * chunk序号
     */
    private int chunkNumber;

    /**
     * 来源名称（文档名称或视频标题）
     */
    private String sourceName;

}
