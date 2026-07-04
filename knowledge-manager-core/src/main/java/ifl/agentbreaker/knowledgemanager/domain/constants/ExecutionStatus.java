package ifl.agentbreaker.knowledgemanager.domain.constants;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

@Getter
public enum ExecutionStatus
{
    FAILED(0, "失败"),
    SUCCESS(1, "成功");

    @EnumValue
    private final int code;
    private final String description;

    ExecutionStatus(int code, String description)
    {
        this.code = code;
        this.description = description;
    }
}
