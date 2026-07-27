package ifl.agentbreaker.knowledgemanager.domain.entities.pg;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class DocumentChunkBase extends KnowledgeBaseChunkBase
{
    long documentId;
    String sectionNumber;
    String previousSectionAbstract;
    String nextSectionAbstract;
    String introduction;
    List<String> tags;
    List<Long> referencedImageChunkIds;
}
