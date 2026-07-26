package ifl.agentbreaker.knowledgemanager.domain.dtos.requests;

import lombok.Data;

@Data
public class UpdateCrawlTaskEnableStatusRequest
{
    /**
     * 爬取任务状态 - 是否启用
     */
    private boolean enabled;

    /**
     * 爬取任务ID
     */
    private long crawlTaskId;
}
