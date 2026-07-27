package ifl.agentbreaker.knowledgemanager.domain.dtos.responses;

import ifl.agentbreaker.knowledgemanager.domain.constants.ChunkType;
import lombok.Data;

import java.util.Date;

@Data
public class ChunkAbstract
{

    /**
     * chunk ID
     */
    private long chunkId;

    /**
     * 所属知识库
     */
    private long knowledgeBaseId;

    /**
     * chunk来源实体
     */
    private ChunkType chunkType;

    /**
     * chunk序号
     */
    private int chunkNumber;

    /**
     * 内容摘要
     */
    private String chunkPreview;

    /**
     * token数量
     */
    private int tokenCount;

    /**
     * 创建时间
     */
    // TODO: Use "java.time.Instant" instead of "java.util.Date".
    private Date creationTime;

}
