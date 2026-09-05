package ifl.agentbreaker.knowledgemanager.domain.dtos.document;

import lombok.Data;

@Data
public class CaptionBlock
{
    /**
     * Caption type.
     */
    private CaptionType captionType;

    /**
     * Complete caption content.
     */
    private String content;

    /**
     * Start line index in the page text.(0-based index)
     */
    private int startLine;

    /**
     * End line index in the page text.(0-based index)
     */
    private int endLine;
}
