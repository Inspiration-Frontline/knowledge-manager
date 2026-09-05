package ifl.agentbreaker.knowledgemanager.domain.dtos.document;

import lombok.AllArgsConstructor;
import lombok.Data;

// A recognized section header.
@Data
@AllArgsConstructor
public class SectionHeader
{
    private String sectionNumber;

    private String title;
}
