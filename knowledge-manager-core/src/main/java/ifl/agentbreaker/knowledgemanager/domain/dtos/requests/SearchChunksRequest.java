package ifl.agentbreaker.knowledgemanager.domain.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SearchChunksRequest
{
    /**
     * 知识库ID
     */
    private long knowledgeBaseId;

    /**
     * 查询内容
     */
    @NotBlank
    private String query;
}
