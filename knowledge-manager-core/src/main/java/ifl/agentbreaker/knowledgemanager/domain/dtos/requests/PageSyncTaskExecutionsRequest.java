package ifl.agentbreaker.knowledgemanager.domain.dtos.requests;

import ifl.agentbreaker.knowledgemanager.domain.constants.ExecutionStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class PageSyncTaskExecutionsRequest extends PageRequest
{
    /**
     * 同步任务ID
     */
    private long syncTaskId;

    /**
     * 执行状态
     */
    private ExecutionStatus executionStatus;
}
