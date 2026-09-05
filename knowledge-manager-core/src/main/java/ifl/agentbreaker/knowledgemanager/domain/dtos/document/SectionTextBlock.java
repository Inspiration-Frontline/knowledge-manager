package ifl.agentbreaker.knowledgemanager.domain.dtos.document;

import lombok.AllArgsConstructor;
import lombok.Data;

// A continuous text area belonging to the same section on a single page.(Header or text)
@Data
@AllArgsConstructor
public class SectionTextBlock
{
    private String sectionNumber;

    private String content;

    private boolean newSection;
}
