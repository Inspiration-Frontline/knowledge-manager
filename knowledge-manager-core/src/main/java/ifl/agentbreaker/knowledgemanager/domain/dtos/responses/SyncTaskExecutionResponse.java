package ifl.agentbreaker.knowledgemanager.domain.dtos.responses;

import ifl.agentbreaker.knowledgemanager.domain.constants.ExecutionStatus;

import java.util.Date;

public class SyncTaskExecutionResponse
{
    /**
     * 同步任务执行记录ID
     */
    private long syncTaskExecutionId;

    /**
     * 开始时间
     */
    private Date startTime;

    /**
     * 结束时间
     */
    private Date finishTime;

    /**
     * 执行耗时（ms）
     */
    private long durationMs;

    /**
     * 执行状态
     */
    private ExecutionStatus crawlStatus;

    /**
     * 执行信息
     */
    private String crawlMessage;
}
