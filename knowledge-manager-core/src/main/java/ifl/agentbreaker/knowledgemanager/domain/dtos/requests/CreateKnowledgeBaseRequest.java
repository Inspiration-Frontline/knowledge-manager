package ifl.agentbreaker.knowledgemanager.domain.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreateKnowledgeBaseRequest
{
    /**
     * 所属业务ID
     */
    private long bizId;

    /**
     * 知识库名称
     */
    @NotBlank
    private String name;

    /**
     * 知识库描述
     */
    private String description;

    /**
     * Embedding模型
     */
    @NotBlank
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
     * TopK召回数量
     */
    private int topK;

    /**
     * 相似度阈值
     */
    @NotNull
    private BigDecimal similarityThreshold;
}
