package ifl.agentbreaker.knowledgemanager.domain.dtos.responses;

import lombok.Data;

import java.util.Date;
import java.util.List;

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
    private int chunkNo;

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

}
