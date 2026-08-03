package ifl.agentbreaker.knowledgemanager.domain.dtos.responses;

import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
public class DocumentChunkDetail
{
    /**
     * Chunk ID
     */
    private long chunkId;

    /**
     * 文档知识库ID
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
     * Chunk序号
     */
    private int chunkNumber;

    /**
     * Token数量
     */
    private int tokenCount;

    /**
     * Chunk内容
     */
    private String chunkContent;

    /**
     * 所属章节
     */
    private String sectionNumber;

    /**
     * 前置Chunk摘要
     */
    private String previousChunkAbstract;

    /**
     * 后置Chunk摘要
     */
    private String nextChunkAbstract;

    /**
     * 引用图片Chunk ID
     */
    private List<Long> referencedImageChunkIds;

    /**
     * 创建时间
     */
    private Instant creationTime;
}
