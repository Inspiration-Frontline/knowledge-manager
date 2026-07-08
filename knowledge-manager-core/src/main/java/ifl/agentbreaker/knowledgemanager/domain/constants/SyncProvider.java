package ifl.agentbreaker.knowledgemanager.domain.constants;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;

public enum SyncProvider
{
    FEISHU(0, "飞书");

    @EnumValue
    @JsonValue
    private final int code;
    private final String description;

    SyncProvider(int code, String description)
    {
        this.code = code;
        this.description = description;
    }
}
