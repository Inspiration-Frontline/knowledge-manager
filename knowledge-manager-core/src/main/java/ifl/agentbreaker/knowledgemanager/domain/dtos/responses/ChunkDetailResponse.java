package ifl.agentbreaker.knowledgemanager.domain.dtos.responses;

import lombok.Data;

import java.util.Date;

@Data
public class ChunkDetailResponse
{
    /**
     * chunk ID
     */
    private long chunkId;

    /**
     * 知识库ID
     */
    private long knowledgeBaseId;

    // ChunkType
    // id

    /**
     * 文档ID
     */
    private Long documentId;

    /**
     * 视频ID
     */
    private Long videoId;

    /**
     * chunk序号
     */
    // TODO: DO NOT use "no" for short of "number"
    private int chunkNumber;

    /**
     * chunk内容
     */
    private String chunkContent;

    /**
     * token数量
     */
    private int tokenCount;

    /**
     * 创建时间
     */
    private Date creationTime;

    // float[] embedding;
}
