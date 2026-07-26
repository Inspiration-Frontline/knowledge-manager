package ifl.agentbreaker.knowledgemanager.domain.dtos.requests;

import ifl.agentbreaker.knowledgemanager.domain.constants.ExecutionStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class PageCrawlTaskExecutionsRequest extends PageRequest
{
    /**
     * 爬取任务ID
     */
    private long crawlTaskId;

    /**
     * 执行状态
     */
    private ExecutionStatus executionStatus;
}
