package ifl.agentbreaker.knowledgemanager.domain.dtos.requests;

import ifl.agentbreaker.knowledgemanager.domain.constants.SyncProvider;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateSyncTaskRequest
{
    /**
     * 任务ID
     */
    private long syncTaskId;

    /**
     * 任务名称
     */
    private String taskName;

    /**
     * Cron表达式
     */
    private String cronExpression;
}
