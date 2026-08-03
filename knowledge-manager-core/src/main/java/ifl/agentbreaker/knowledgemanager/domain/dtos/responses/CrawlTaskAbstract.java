package ifl.agentbreaker.knowledgemanager.domain.dtos.responses;

import lombok.Data;

import java.time.Instant;

@Data
public class CrawlTaskAbstract
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
    private Instant modificationTime;

}
