package ifl.agentbreaker.knowledgemanager.domain.entities.pg;

import ifl.agentbreaker.knowledgemanager.domain.constants.ExecutionStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
public class CrawlTaskExecution extends EntityBase
{
    /**
     * 爬取任务ID
     */
    private long crawlTaskId;

    /**
     * 开始时间
     */
    private Date startTime;

    /**
     * 结束时间
     */
    private Date finishTime;

    /**
     * 执行状态
     */
    private ExecutionStatus crawlStatus;

    /**
     * 执行信息
     */
    private String crawlMessage;
}
