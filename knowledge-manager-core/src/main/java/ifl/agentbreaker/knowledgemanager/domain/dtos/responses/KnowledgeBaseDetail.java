package ifl.agentbreaker.knowledgemanager.domain.dtos.responses;

import ifl.agentbreaker.knowledgemanager.domain.constants.ChunkType;
import lombok.Data;

import java.time.Instant;

@Data
public class KnowledgeBaseDetail
{
    /**
     * 知识库ID
     */
    private long knowledgeBaseId;

    /**
     * 所属业务ID
     */
    private long bizId;

    /**
     * 知识库名称
     */
    private String name;

    /**
     * 知识库chunk类型 - 文档 or 图片 or 视频
     */
    private ChunkType chunkType;

    /**
     * 描述
     */
    private String description;

    /**
     * Embedding模型
     */
    private String embeddingModel;

    /**
     * 向量维度
     */
    private int embeddingDimensionCount;

    /**
     * 状态
     */
    private boolean enabled;

    /**
     * Chunk大小
     */
    private Integer maxChunkSize;

    /**
     * Chunk重叠
     */
    private Integer chunkOverlap;

    /**
     * 创建时间
     */
    private Instant creationTime;

    /**
     * 更新时间
     */
    private Instant modificationTime;

    /**
     * chunk数量
     */
    private long chunkCount;
}
