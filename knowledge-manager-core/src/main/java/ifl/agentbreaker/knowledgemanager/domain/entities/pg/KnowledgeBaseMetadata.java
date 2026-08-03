package ifl.agentbreaker.knowledgemanager.domain.entities.pg;

import ifl.agentbreaker.knowledgemanager.domain.constants.ChunkType;
import lombok.Data;
import lombok.EqualsAndHashCode;

// This is the metadata of a knowledge base.
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
     * 知识库名称（小写英文） - 知识库名称用于识别知识库
     */
    private String name;

    /**
     * 知识库chunk类型 - 文档 or 图片 or 视频
     */
    private ChunkType chunkType;

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
     * 知识库状态 - 是否启用
     */
    private boolean enabled;

    /**
     * chunk大小 - 按字符数来切的（切完之后再embedding成token） 注意不会硬切，需要保证句子的完整性或者段落的完整性（图片、视频知识库为null）
     */
    private Integer minChunkSize;

    /**
     * chunk重叠长度 - 例如100token重叠 （图片、视频知识库为null）
     */
    private Integer chunkOverlap;

    // TODO: 这两个不应该在创建知识库的时候写死，而是写在nacos配置中心
    /**
     * 召回数量 - 例如取最相似的5个chunk
     */
//    private int topK;

    /**
     * 相似度阈值 - 例如要求高于0.75相似度
     */
//    private BigDecimal similarityThreshold;
}
