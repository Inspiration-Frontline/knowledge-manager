package ifl.agentbreaker.knowledgemanager.domain.dtos.requests;

import ifl.agentbreaker.knowledgemanager.domain.constants.Status;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class PageKnowledgeBasesRequest extends PageRequest
{
    /**
     * 所属业务ID
     */
    private Long bizId;

    /**
     * 名称（模糊查询）
     */
    private String keyword;

    /**
     * 状态
     */
    private Status status;
}
