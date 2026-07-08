package ifl.agentbreaker.knowledgemanager.domain.dtos.responses;

import lombok.Data;

import java.util.Date;

@Data
public class ChunkResponse
{

    /**
     * Chunk ID
     */
    private long chunkId;

    /**
     * 所属知识库
     */
    private long knowledgeBaseId;

    /**
     * 来源文档ID
     */
    private Long documentId;

    /**
     * 来源视频ID
     */
    private Long videoId;

    /**
     * Chunk序号
     */
    private int chunkNo;

    /**
     * 内容摘要
     */
    private String chunkPreview;

    /**
     * Token数量
     */
    private int tokenCount;

    /**
     * 创建时间
     */
    private Date creationTime;

}
