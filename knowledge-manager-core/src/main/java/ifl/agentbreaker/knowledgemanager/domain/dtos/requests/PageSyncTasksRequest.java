package ifl.agentbreaker.knowledgemanager.domain.dtos.requests;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class PageSyncTasksRequest extends PageRequest
{
    /**
     * 知识库ID
     */
    private Long knowledgeBaseId;

    /**
     * 关键字（任务名称）
     */
    private String keyword;

    /**
     * 任务状态 - 是否启用
     */
    private boolean enabled;
}
