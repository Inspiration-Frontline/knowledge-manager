package ifl.agentbreaker.knowledgemanager.domain.constants;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;

public enum ChunkType
{
    DOCUMENT(0, "document"),
    DOCUMENT_IMAGE(0, "document"),
    IMAGE(0, "document"),
    VIDEO(1, "video");

    @EnumValue
    @JsonValue
    private final int code;
    private final String description;

    ChunkType(int code, String description)
    {
        this.code = code;
        this.description = description;
    }
}
