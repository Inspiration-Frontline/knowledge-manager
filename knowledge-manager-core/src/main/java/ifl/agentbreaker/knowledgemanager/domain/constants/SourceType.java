package ifl.agentbreaker.knowledgemanager.domain.constants;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

@Getter
public enum SourceType
{
    UPLOAD(0, "upload"),
    CRAWL(1, "crawl"),
    FEISHU(2, "feishu");

    @EnumValue
    private final int code;
    private final String description;

    SourceType(int code, String description)
    {
        this.code = code;
        this.description = description;
    }
}
