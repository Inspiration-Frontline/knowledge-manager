package ifl.agentbreaker.knowledgemanager.domain.entities.pg;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class DocumentImageChunkBase extends KnowledgeBaseChunkBase
{
    long documentId;
    int width;
    int height;
    String sectionNumber;
    String description;
    String nameInOss;
}
