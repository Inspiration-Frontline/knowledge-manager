package ifl.agentbreaker.knowledgemanager.domain.dtos.responses;

import ifl.agentbreaker.knowledgemanager.domain.constants.SyncProvider;
import lombok.Data;

import java.time.Instant;

@Data
public class SyncTaskAbstract
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
     * 任务状态 - 是否启用
     */
    private boolean enabled;

    /**
     * 更新时间
     */
    private Instant modificationTime;
}
