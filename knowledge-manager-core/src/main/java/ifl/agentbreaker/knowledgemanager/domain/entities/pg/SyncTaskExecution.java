package ifl.agentbreaker.knowledgemanager.domain.entities.pg;

import ifl.agentbreaker.knowledgemanager.domain.constants.ExecutionStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.Instant;

@Data
@EqualsAndHashCode(callSuper = true)
public class SyncTaskExecution extends EntityBase
{
    /**
     * 同步任务ID
     */
    private long syncTaskId;

    /**
     * 开始时间
     */
    private Instant startTime;

    /**
     * 结束时间
     */
    private Instant finishTime;

    /**
     * 执行状态
     */
    private ExecutionStatus executionStatus;

    /**
     * 执行信息
     */
    private String executionMessage;
}
