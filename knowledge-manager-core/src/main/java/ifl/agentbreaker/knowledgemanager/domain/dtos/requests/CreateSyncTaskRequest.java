package ifl.agentbreaker.knowledgemanager.domain.dtos.requests;

import ifl.agentbreaker.knowledgemanager.domain.constants.SyncProvider;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateSyncTaskRequest
{
    /**
     * 知识库ID
     */
    private long knowledgeBaseId;

    /**
     * 任务名称
     */
    @NotBlank
    private String taskName;

    /**
     * 同步源
     */
    @NotNull
    private SyncProvider provider;

    // workspace id

    /**
     * Cron表达式
     */
    @NotBlank
    private String cronExpression;
}
