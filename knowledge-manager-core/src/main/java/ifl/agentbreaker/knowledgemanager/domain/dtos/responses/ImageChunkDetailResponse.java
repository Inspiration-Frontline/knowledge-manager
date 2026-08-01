package ifl.agentbreaker.knowledgemanager.domain.dtos.responses;

import lombok.Data;

import java.time.Instant;

@Data
public class ImageChunkDetailResponse
{
    /**
     * 图片Chunk ID
     */
    private long chunkId;

    /**
     * 图片知识库ID
     */
    private long knowledgeBaseId;

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
     * 图片OSS地址
     * 实际返回临时访问地址
     */
    private String imageUrl;

    /**
     * Token数量
     */
    private int tokenCount;

    /**
     * 创建时间
     */
    private Instant creationTime;
}
