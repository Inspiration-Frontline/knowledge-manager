package ifl.agentbreaker.knowledgemanager.domain.entities.pg;

import ifl.agentbreaker.knowledgemanager.domain.constants.SyncProvider;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper = true)
public class SyncTask extends EntityBase
{
    /**
     * 知识库ID - 关联知识库
     */
    private long knowledgeBaseId;

    /**
     * 同步任务名称 - 便于管理
     */
    private String taskName;

    /**
     * 同步源 - 暂时只有feishu
     */
    private SyncProvider provider;

    /**
     * 同步源知识库ID
     */
    private String workspaceId;

    /**
     * cron表达式 - 定时执行的规则
     */
    private String cronExpression;

    /**
     * 同步任务状态 - 是否启用
     */
    private boolean enabled;
}
