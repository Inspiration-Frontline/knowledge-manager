package ifl.agentbreaker.knowledgemanager.domain.dtos.responses;

import ifl.agentbreaker.knowledgemanager.domain.constants.EmbeddingTaskStatus;
import lombok.Data;

import java.util.Date;

@Data
public class EmbeddingTaskDetailResponse
{

    /**
     * 任务ID
     */
    private long embeddingTaskId;

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
     * 来源名称（文档名称或视频标题）
     */
    private String sourceName;

    /**
     * 当前状态
     */
    private EmbeddingTaskStatus status;

    /**
     * 重试次数
     */
    private int retryCount;

    /**
     * 错误信息
     */
    private String errorMessage;

    /**
     * 开始时间
     */
    private Date startTime;

    /**
     * 完成时间
     */
    private Date finishTime;

    /**
     * 耗时（毫秒）
     */
    private Long durationMs;

    /**
     * 创建时间
     */
    private Date creationTime;

}
