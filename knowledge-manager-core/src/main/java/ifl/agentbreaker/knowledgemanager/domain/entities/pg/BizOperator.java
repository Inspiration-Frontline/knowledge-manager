package ifl.agentbreaker.knowledgemanager.domain.entities.pg;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class BizOperator extends EntityBase
{
    /**
     * 知识库ID - 关联知识库
     */
    private long knowledgeBaseId;

    /**
     * 业务ID - 关联业务
     */
    private long bizId;

    /**
     * 用户ID - 这张表用来存每个业务系统下的每个知识库有哪些人，分别有什么样的操作权限
     */
    private long userId;

    /**
     * 权限 - 存储该知识库的用户权限信息
     */
    private List<String> permissions;
}
