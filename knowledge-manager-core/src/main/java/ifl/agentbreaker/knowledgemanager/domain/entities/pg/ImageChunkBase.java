package ifl.agentbreaker.knowledgemanager.domain.entities.pg;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ImageChunkBase extends KnowledgeBaseChunkBase
{
    int width;
    int height;
    String imageUrl;
}
