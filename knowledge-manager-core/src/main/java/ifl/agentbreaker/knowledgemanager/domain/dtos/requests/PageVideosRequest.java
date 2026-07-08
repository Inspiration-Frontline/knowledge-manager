package ifl.agentbreaker.knowledgemanager.domain.dtos.requests;

import ifl.agentbreaker.knowledgemanager.domain.constants.ParseStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class PageVideosRequest extends PageRequest
{

    /**
     * 所属知识库
     */
    private long knowledgeBaseId;

    /**
     * 标题（模糊查询）
     */
    private String keyword;

    /**
     * 解析状态
     */
    private ParseStatus parseStatus;

}
