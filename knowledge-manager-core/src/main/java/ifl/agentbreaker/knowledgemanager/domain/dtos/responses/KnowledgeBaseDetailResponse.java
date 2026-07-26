package ifl.agentbreaker.knowledgemanager.domain.dtos.responses;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class KnowledgeBaseDetailResponse
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
    private String knowledgeBaseName;

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
    private int embeddingDimensions;

    /**
     * 状态
     */
    private EnableStatus enableStatus;

    /**
     * Chunk大小
     */
    private int chunkSize;

    /**
     * Chunk重叠
     */
    private int chunkOverlap;

    /**
     * TopK
     */
    private int topK;

    /**
     * 相似度阈值
     */
    private BigDecimal similarityThreshold;

    /**
     * 创建时间
     */
    private Date creationTime;

    /**
     * 更新时间
     */
    private Date modificationTime;

    /**
     * 文档数量
     */
    private int documentCount;

    /**
     * 视频数量
     */
    private int videoCount;
}
