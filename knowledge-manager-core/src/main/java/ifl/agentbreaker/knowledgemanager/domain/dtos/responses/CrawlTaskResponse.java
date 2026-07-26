package ifl.agentbreaker.knowledgemanager.domain.dtos.responses;

import ifl.agentbreaker.knowledgemanager.domain.constants.ExecutionStatus;
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
     * 任务状态 - 是否启用
     */
    private boolean enabled;

    /**
     * 更新时间
     */
    private Date modificationTime;

}
