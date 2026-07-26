package ifl.agentbreaker.knowledgemanager.domain.entities.pg;

import ifl.agentbreaker.knowledgemanager.domain.constants.ExecutionStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

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
    private ExecutionStatus syncStatus;

    /**
     * 执行信息
     */
    private String syncMessage;


}
