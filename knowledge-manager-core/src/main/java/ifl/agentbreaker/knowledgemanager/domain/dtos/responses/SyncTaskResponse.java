package ifl.agentbreaker.knowledgemanager.domain.dtos.responses;

import ifl.agentbreaker.knowledgemanager.domain.constants.ExecutionStatus;
import ifl.agentbreaker.knowledgemanager.domain.constants.EnableStatus;
import ifl.agentbreaker.knowledgemanager.domain.constants.SyncProvider;
import lombok.Data;

import java.util.Date;

@Data
public class SyncTaskResponse
{
    /**
     * 任务ID
     */
    private long syncTaskId;

    /**
     * 知识库ID
     */
    private long knowledgeBaseId;

    /**
     * 任务名称
     */
    private String taskName;

    /**
     * 同步源
     */
    private SyncProvider provider;

    /**
     * 状态（启用/停用）
     */
    private EnableStatus enableStatus;

    /**
     * 最后同步时间
     */
    private Date lastSyncTime;

    /**
     * 最后同步状态
     */
    private ExecutionStatus lastSyncStatus;

    /**
     * 更新时间
     */
    private Date modificationTime;
}
