package ifl.agentbreaker.knowledgemanager.domain.dtos.responses;

import ifl.agentbreaker.knowledgemanager.domain.constants.ExecutionStatus;
import ifl.agentbreaker.knowledgemanager.domain.constants.EnableStatus;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class CrawlTaskDetailResponse
{

    /**
     * 任务ID
     */
    private long crawlTaskId;

    /**
     * 所属知识库
     */
    private long knowledgeBaseId;

    /**
     * 任务名称
     */
    private String taskName;

    /**
     * 起始URL列表
     */
    private List<String> startUrls;

    /**
     * Cron表达式
     */
    private String cronExpression;

    /**
     * 最大爬取深度
     */
    private int maxDepth;

    /**
     * 任务状态（启用/停用）
     */
    private EnableStatus status;

    /**
     * 最后爬取时间
     */
    private Date lastCrawlTime;

    /**
     * 最后爬取状态
     */
    private ExecutionStatus lastCrawlStatus;

    /**
     * 最后执行信息
     */
    private String lastCrawlMessage;

    /**
     * 创建时间
     */
    private Date creationTime;

    /**
     * 修改时间
     */
    private Date modificationTime;

}
