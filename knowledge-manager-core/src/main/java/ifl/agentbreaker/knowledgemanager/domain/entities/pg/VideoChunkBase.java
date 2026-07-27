package ifl.agentbreaker.knowledgemanager.domain.entities.pg;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class VideoChunkBase extends KnowledgeBaseChunkBase
{
    String videoUrl;
    int width;
    int height;
    String title;
    String introduction;
    List<String> tags;
}
