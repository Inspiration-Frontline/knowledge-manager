package ifl.agentbreaker.knowledgemanager.domain.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ImportImageRequest
{
    /**
     * 图片知识库ID
     */
    private long knowledgeBaseId;

    /**
     * 图片
     */
    @NotBlank
    private String imageUrl;
}
