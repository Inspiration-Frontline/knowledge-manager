package ifl.agentbreaker.knowledgemanager.domain.constants;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

@Getter
public enum DocumentType
{
    PDF(0, "PDF"),
    HTML(1, "HTML"),
    MARKDOWN(2, "Markdown"),
    WORD(3, "Word");

    @EnumValue
    private final int code;
    private final String description;

    DocumentType(int code, String description)
    {
        this.code = code;
        this.description = description;
    }
}
