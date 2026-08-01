package ifl.agentbreaker.knowledgemanager.domain.dtos.requests;

import jakarta.validation.constraints.Min;
import lombok.Data;

import java.util.List;

@Data
public class UpdateCrawlTaskRequest
{

    /**
     * 任务ID
     */
    private long crawlTaskId;

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
     * 最大深度
     */
    @Min(0)
    private int maxDepth;

}
