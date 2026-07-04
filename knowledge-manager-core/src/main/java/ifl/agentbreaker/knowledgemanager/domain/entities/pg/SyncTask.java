package ifl.agentbreaker.knowledgemanager.domain.entities.pg;

import ifl.agentbreaker.knowledgemanager.domain.constants.ExecutionStatus;
import ifl.agentbreaker.knowledgemanager.domain.constants.Status;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

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
    private String name;

    /**
     * 同步源 - 暂时只有feishu
     */
    private String provider;

    /**
     * cron表达式 - 定时执行的规则
     */
    private String cronExpression;

    /**
     * 同步任务状态 - 0停用 1启用
     */
    private Status status;

    /**
     * 最后同步的时间 - 用于监控同步任务运行
     */
    private Date lastSyncTime;

    /**
     * 最后同步的状态 - 0失败 1成功
     */
    private ExecutionStatus lastSyncStatus;

    /**
     * 最后同步的信息 - 记录同步任务执行的结果信息或异常信息
     */
    private String lastSyncMessage;
}
