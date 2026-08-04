package ifl.agentbreaker.knowledgemanager.domain.entities.pg;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class CrawlTask extends EntityBase
{
    /**
     * 知识库ID - 关联知识库
     */
    private long knowledgeBaseId;

    /**
     * 爬取任务名称 - 便于管理，同一知识库不能有同名任务
     */
    private String taskName;

    /**
     * 起始URL列表 - 多个入口网址
     */
    private List<String> startUrls;

    /**
     * Cron表达式 - 定时执行的规则
     */
    private String cronExpression;

    /**
     * 最大爬取深度 - 限制爬取范围
     */
    private int maxDepth;

    /**
     * 爬取任务状态 - 是否启用
     */
    private boolean enabled;
}
