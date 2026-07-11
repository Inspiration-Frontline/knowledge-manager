package ifl.agentbreaker.knowledgemanager.domain.constants;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

// TODO: Delete this enum, use "boolean enabled" instead.
@Getter
public enum EnableStatus
{
    DISABLED(0, "停用"),
    ENABLED(1, "启用");

    @EnumValue
    @JsonValue
    private final int code;
    private final String description;

    EnableStatus(int code, String description)
    {
        this.code = code;
        this.description = description;
    }
}