package ifl.agentbreaker.knowledgemanager.domain.dtos.requests;

import lombok.Data;

@Data
public class UpdateSyncTaskEnableStatusRequest
{
    /**
     * 同步任务状态 - 是否启用
     */
    private boolean enabled;

    /**
     * 同步任务ID
     */
    private long syncTaskId;
}
