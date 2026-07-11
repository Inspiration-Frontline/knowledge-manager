package ifl.agentbreaker.knowledgemanager.domain.dtos.requests;

import ifl.agentbreaker.knowledgemanager.domain.constants.EnableStatus;
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
     * 状态（启用/停用）
     */
    private EnableStatus enableStatus;
}
