package ifl.agentbreaker.knowledgemanager.domain.dtos.document;

import lombok.Data;

@Data
public class ParsedTable
{
    /**
     * Table caption.
     */
    private String content;

    /**
     * Table content in Markdown format.
     */
    private String markdown;
}
