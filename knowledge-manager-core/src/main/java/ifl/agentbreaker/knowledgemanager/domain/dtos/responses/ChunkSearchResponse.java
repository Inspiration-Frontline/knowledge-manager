package ifl.agentbreaker.knowledgemanager.domain.dtos.responses;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

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
     * Chunk内容
     */
    private String chunkContent;

    /**
     * 来源文档
     */
    private Long documentId;

    /**
     * 来源视频
     */
    private Long videoId;

    /**
     * chunk序号
     */
    private int chunkNo;

    /**
     * 来源名称（文档名称或视频标题）
     */
    private String sourceName;

}
