package ifl.agentbreaker.knowledgemanager.domain.entities.pg;

import ifl.agentbreaker.knowledgemanager.domain.constants.EmbeddingTaskStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
public class EmbeddingTask extends EntityBase
{
    /**
     * 文档ID - 来源文档（与video_id不同时非空）
     */
    private Long documentId;

    /**
     * 视频ID - 来源视频（与document_id不同时非空）
     */
    private Long videoId;

    /**
     * Embedding任务状态 - 0待执行 1执行中 2成功 3失败
     */
    private EmbeddingTaskStatus status;

    /**
     * 重试次数 - 用于重试机制
     */
    private int retryCount;

    /**
     * 错误信息 - 用于失败记录
     */
    private String errorMessage;

    /**
     * 开始时间 - 用于统计耗时和性能
     */
    private Date startTime;

    /**
     * 完成时间 - 用于统计耗时和性能
     */
    private Date finishTime;
}
