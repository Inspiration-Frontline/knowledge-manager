package ifl.agentbreaker.knowledgemanager.domain.constants;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum ParsingStatus
{
    UNPARSED(0, "未解析"),
    SUCCESS(1, "成功"),
    FAILED(2, "失败");

    @EnumValue
    @JsonValue
    private final int code;
    private final String description;

    ParsingStatus(int code, String description)
    {
        this.code = code;
        this.description = description;
    }
}
