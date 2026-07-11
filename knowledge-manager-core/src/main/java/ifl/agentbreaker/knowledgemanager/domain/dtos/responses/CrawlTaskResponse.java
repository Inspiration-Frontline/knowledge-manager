package ifl.agentbreaker.knowledgemanager.domain.dtos.responses;

import ifl.agentbreaker.knowledgemanager.domain.constants.ExecutionStatus;
import ifl.agentbreaker.knowledgemanager.domain.constants.EnableStatus;
import lombok.Data;

import java.util.Date;

@Data
public class CrawlTaskResponse
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
     * 任务状态（启用/停用）
     */
    private EnableStatus enableStatus;

    /**
     * 最后爬取时间
     */
    private Date lastCrawlTime;

    /**
     * 最后爬取状态
     */
    private ExecutionStatus lastCrawlStatus;

    /**
     * 修改时间
     */
    private Date modificationTime;

}
