package ifl.agentbreaker.knowledgemanager.domain.entities.pg;

import ifl.agentbreaker.knowledgemanager.domain.constants.ExecutionStatus;
import ifl.agentbreaker.knowledgemanager.domain.constants.EnableStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
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
     * 爬取任务名称 - 便于管理
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
     * 爬取任务状态 - 0停用 1启用
     */
    private EnableStatus enableStatus;

    // TODO: Put below fields into another entity => CrawlTaskExecution.

    /**
     * 最后爬取的时间 - 用于爬取监控任务运行
     */
    private Date lastCrawlTime;

    /**
     * 最后爬取的状态 - 0失败 1成功
     */
    private ExecutionStatus lastCrawlStatus;

    /**
     * 最后爬取的信息 - 记录爬取任务执行的结果信息或异常信息
     */
    private String lastCrawlMessage;
}
