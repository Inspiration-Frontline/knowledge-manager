package ifl.agentbreaker.knowledgemanager.domain.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;


@Data
public class ImportVideoRequest
{

    /**
     * 视频知识库ID
     */
    private long knowledgeBaseId;

    /**
     * 视频链接
     */
    @NotBlank
    private String videoUrl;

}