package ifl.agentbreaker.knowledgemanager.domain.dtos.responses;

import lombok.Data;

import java.time.Instant;

@Data
public class ImageChunkResponse
{
    /**
     * 图片Chunk ID
     */
    private long chunkId;

    /**
     * 图片内容摘要 - LLM生成
     */
    private String chunkAbstract;

    /**
     * 图片宽度
     */
    private int width;

    /**
     * 图片高度
     */
    private int height;

    /**
     * Token数量
     */
    private int tokenCount;

    /**
     * 创建时间
     */
    private Instant creationTime;
}
