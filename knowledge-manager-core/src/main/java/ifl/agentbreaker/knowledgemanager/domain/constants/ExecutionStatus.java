package ifl.agentbreaker.knowledgemanager.domain.constants;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum ExecutionStatus
{
    PENDING(0, "待执行"),
    IN_PROGRESS(1, "执行中"),
    SUCCEEDED(2, "成功"),
    FAILED(3, "失败");

    @EnumValue
    @JsonValue
    private final int code;
    private final String description;

    ExecutionStatus(int code, String description)
    {
        this.code = code;
        this.description = description;
    }
}
