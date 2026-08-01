package ifl.agentbreaker.knowledgemanager.domain.dtos.requests;

import lombok.Data;

@Data
public class UpdateKnowledgeBaseEnableStatusRequest
{
    /**
     * 知识库状态 - 是否启用
     */
    private boolean enabled;

    /**
     * 知识库ID
     */
    private long knowledgeBaseId;
}
