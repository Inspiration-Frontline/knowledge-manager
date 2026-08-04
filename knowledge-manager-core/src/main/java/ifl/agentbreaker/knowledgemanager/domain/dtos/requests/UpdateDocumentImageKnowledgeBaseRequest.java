package ifl.agentbreaker.knowledgemanager.domain.dtos.requests;

import lombok.Data;

@Data
public class UpdateDocumentImageKnowledgeBaseRequest
{
    /**
     * 文档图片知识库ID
     */
    private long knowledgeBaseId;

    /**
     * Embedding模型
     */
    private String embeddingModel;

    /**
     * 向量维度
     */
    private Integer embeddingDimensionCount;
}
