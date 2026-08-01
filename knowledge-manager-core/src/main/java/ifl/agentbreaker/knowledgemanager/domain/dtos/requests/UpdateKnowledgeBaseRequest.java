package ifl.agentbreaker.knowledgemanager.domain.dtos.requests;

import lombok.Data;


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
    private int embeddingDimensionCount;

    /**
     * Chunk大小
     */
    private int minChunkSize;

    /**
     * Chunk重叠长度
     */
    private int chunkOverlap;
}
