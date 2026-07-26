package ifl.agentbreaker.knowledgemanager.domain.entities.pg;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class BizInfo extends EntityBase
{
    /**
     * 业务 Api Key - 唯一标识业务系统，可用于权限验证
     */
    private String apiKey;

    /**
     * 业务名称 - 可读业务名称，比如“客户管理系统”
     */
    private String name;

    /**
     * 业务描述 - 对业务的详细说明
     */
    private String description;

    /**
     * 业务状态 - 0停用 1启用
     */
    private EnableStatus enableStatus;
}
