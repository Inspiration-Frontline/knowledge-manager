package ifl.agentbreaker.knowledgemanager.domain.dtos.responses;

import ifl.agentbreaker.knowledgemanager.domain.constants.SyncProvider;
import lombok.Data;

import java.time.Instant;

@Data
public class SyncTaskDetail
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
     * 同步源知识库ID
     */
    private String workspaceId;

    /**
     * Cron表达式
     */
    private String cronExpression;

    /**
     * 任务状态 - 是否启用
     */
    private boolean enabled;

    /**
     * 创建时间
     */
    private Instant creationTime;

    /**
     * 修改时间
     */
    private Instant modificationTime;
}
