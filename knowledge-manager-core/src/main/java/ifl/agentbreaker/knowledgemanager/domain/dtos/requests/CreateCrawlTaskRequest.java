package ifl.agentbreaker.knowledgemanager.domain.dtos.requests;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class CreateCrawlTaskRequest
{

    /**
     * 知识库ID
     */
    private long knowledgeBaseId;

    /**
     * 任务名称
     */
    @NotBlank
    private String taskName;

    /**
     * 起始URL列表
     */
    @NotEmpty
    private List<String> startUrls;

    /**
     * Cron表达式
     */
    @NotBlank
    private String cronExpression;

    /**
     * 最大爬取深度
     */
    @Min(0)
    private int maxDepth;

}
