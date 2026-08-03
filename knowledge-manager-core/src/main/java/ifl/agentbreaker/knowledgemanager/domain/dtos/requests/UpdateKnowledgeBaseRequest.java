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
     * 文档图片Embedding模型
     */
    private String documentImageEmbeddingModel;

    /**
     * 文档图片chunk的向量维度数量
     */
    private Integer documentImageEmbeddingDimensionCount;

    /**
     * Chunk大小
     */
    private Integer minChunkSize;

    /**
     * Chunk重叠长度
     */
    private Integer chunkOverlap;
}
