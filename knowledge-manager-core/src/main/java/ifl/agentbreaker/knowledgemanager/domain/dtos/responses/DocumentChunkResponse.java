package ifl.agentbreaker.knowledgemanager.domain.dtos.responses;

import lombok.Data;

import java.time.Instant;

@Data
public class DocumentChunkResponse
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
     * Chunk序号
     */
    private int chunkNumber;

    /**
     * 所属章节
     */
    private String sectionNumber;

    /**
     * 内容摘要
     */
    private String chunkAbstract;

    /**
     * Token数量
     */
    private int tokenCount;

    /**
     * 创建时间
     */
    private Instant creationTime;
}
