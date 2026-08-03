package ifl.agentbreaker.knowledgemanager.domain.dtos.responses;

import lombok.Data;

import java.time.Instant;

@Data
public class DocumentImageChunkDetail
{
    /**
     * Chunk ID
     */
    private long chunkId;


    /**
     * 文档图片知识库ID
     */
    private long knowledgeBaseId;


    /**
     * 文档ID
     */
    private long documentId;

    /**
     * 文档名称
     */
    private String documentName;

    /**
     * Token数量
     */
    private int tokenCount;

    /**
     * 图片描述 - 文档中对该图片的文字描述
     */
    private String description;

    /**
     * 文档图片内容摘要 - LLM生成
     */
    private String chunkAbstract;

    /**
     * 图片宽度 - 图片像素宽度
     */
    private int width;

    /**
     * 图片高度 - 图片像素高度
     */
    private int height;

    /**
     * 文档图片url - 把文档中的图片截出来后存入oss
     */
    private String nameInOss;

    /**
     * 创建时间
     */
    private Instant creationTime;
}
