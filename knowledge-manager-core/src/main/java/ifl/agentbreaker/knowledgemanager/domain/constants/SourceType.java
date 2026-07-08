package ifl.agentbreaker.knowledgemanager.domain.constants;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum SourceType
{
    UPLOAD(0, "upload"),
    CRAWL(1, "crawl"),
    SYNC(2, "sync");

    @EnumValue
    @JsonValue
    private final int code;
    private final String description;

    SourceType(int code, String description)
    {
        this.code = code;
        this.description = description;
    }
}
