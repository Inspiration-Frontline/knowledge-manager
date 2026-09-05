package ifl.agentbreaker.knowledgemanager.domain.dtos.requests;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class UploadDocumentsRequest
{
    /**
     * 文档知识库ID
     */
    private long documentKnowledgeBaseId;

    /**
     * 文档图片知识库ID
     */
    private long documentImageKnowledgeBaseId;

    /**
     * 文件
     */
    @NotEmpty
    private List<DocumentMetadata> documents;
}