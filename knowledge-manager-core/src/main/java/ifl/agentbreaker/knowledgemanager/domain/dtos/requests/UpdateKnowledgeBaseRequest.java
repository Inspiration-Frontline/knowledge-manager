package ifl.agentbreaker.knowledgemanager.domain.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class UpdateKnowledgeBaseRequest
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
     * 知识库描述
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
     * Chunk大小
     */
    private int chunkSize;

    /**
     * Chunk重叠长度
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
}
