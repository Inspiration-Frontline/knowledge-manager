package ifl.agentbreaker.knowledgemanager.domain.dtos.responses;

import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
public class CrawlTaskDetail
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
