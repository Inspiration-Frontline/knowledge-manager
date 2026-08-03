package ifl.agentbreaker.knowledgemanager.domain.dtos.responses;

import lombok.Data;

import java.time.Instant;

@Data
public class DocumentImageChunkAbstract
{
    /**
     * Chunk ID
     */
    private long chunkId;

    /**
     * 文档ID
     */
    private long documentId;

    /**
     * 文档图片内容摘要 - LLM生成
     */
    private String chunkAbstract;

    /**
     * 图片宽度 - 图片像素宽度
     */
    private int width;

    /**
     * 图片高度 - 图片像素高度
     */
    private int height;

    /**
     * 创建时间
     */
    private Instant creationTime;
}
