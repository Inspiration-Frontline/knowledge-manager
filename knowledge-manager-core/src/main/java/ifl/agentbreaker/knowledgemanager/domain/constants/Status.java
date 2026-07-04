package ifl.agentbreaker.knowledgemanager.domain.constants;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

@Getter
public enum Status
{
    DISABLED(0, "停用"),
    ENABLED(1, "启用");

    @EnumValue
    private final int code;
    private final String description;

    Status(int code, String description)
    {
        this.code = code;
        this.description = description;
    }
}