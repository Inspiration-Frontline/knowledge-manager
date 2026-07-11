package ifl.agentbreaker.knowledgemanager.domain.entities.pg;

import ifl.agentbreaker.knowledgemanager.domain.constants.EnableStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;

// TODO: This is the metadata of a knowledge base.
// You need to create knowledge bases dynamically based on this metadata.
@Data
@EqualsAndHashCode(callSuper = true)
public class KnowledgeBaseMetadata extends EntityBase
{
    /**
     * 业务ID - 关联业务
     */
    private long bizId;

    /**
     * 知识库名称 - 知识库名称用于识别知识库
     */
    private String name;

    /**
     * 描述 - 知识库介绍
     */
    private String description;

    /**
     * Embedding模型 - 比如bge-m3、text-embedding-v3等
     */
    private String embeddingModel;

    /**
     * 向量维度 - 比如1024、1536等
     */
    private int embeddingDimensionCount;

    /**
     * 状态 - 0禁用 1启用
     */
    private EnableStatus enableStatus;

    /**
     * Chunk大小 - 字符数
     */
    private int minChunkSize;

    /**
     * Chunk重叠长度 - 例如100token重叠
     */
    private int chunkOverlap;

    /**
     * 召回数量 - 例如取最相似的5个chunk
     */
//    private int topK;

    /**
     * 相似度阈值 - 例如要求高于0.75相似度
     */
//    private BigDecimal similarityThreshold;
}
