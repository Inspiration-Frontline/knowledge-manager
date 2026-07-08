package ifl.agentbreaker.knowledgemanager.domain.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;


@Data
public class ImportVideoRequest
{

    /**
     * 所属知识库
     */
    private long knowledgeBaseId;

    /**
     * 视频链接
     */
    @NotBlank
    private String url;

}