package ifl.agentbreaker.knowledgemanager.domain.constants;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum EmbeddingTaskStatus
{
    PENDING(0, "待执行"),
    RUNNING(1, "执行中"),
    SUCCESS(2, "成功"),
    FAILED(3, "失败");

    @EnumValue
    @JsonValue
    private final int code;
    private final String description;

    EmbeddingTaskStatus(int code, String description)
    {
        this.code = code;
        this.description = description;
    }
}
