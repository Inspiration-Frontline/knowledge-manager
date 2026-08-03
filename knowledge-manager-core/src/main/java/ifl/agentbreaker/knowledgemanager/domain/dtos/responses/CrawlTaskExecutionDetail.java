package ifl.agentbreaker.knowledgemanager.domain.dtos.responses;

import ifl.agentbreaker.knowledgemanager.domain.constants.ExecutionStatus;
import lombok.Data;

import java.time.Instant;

@Data
public class CrawlTaskExecutionDetail
{
    /**
     * 爬取任务执行记录ID
     */
    private long crawlTaskExecutionId;

    /**
     * 开始时间
     */
    private Instant startTime;

    /**
     * 结束时间
     */
    private Instant finishTime;

    /**
     * 执行耗时（ms）
     */
    private long durationMs;

    /**
     * 执行状态
     */
    private ExecutionStatus executionStatus;

    /**
     * 执行信息
     */
    private String executionMessage;
}
