package ifl.agentbreaker.knowledgemanager.domain.dtos.responses;

import lombok.Data;

import java.util.Date;

@Data
public class KnowledgeBaseResponse
{
    /**
     * 知识库ID
     */
    private long knowledgeBaseId;

    /**
     * 所属业务ID
     */
    private long bizId;

    /**
     * 知识库名称
     */
    private String knowledgeBaseName;

    /**
     * 描述
     */
    private String description;

    /**
     * Embedding模型
     */
    private String embeddingModel;

    /**
     * 状态
     */
    private EnableStatus enableStatus;

    /**
     * 更新时间
     */
    private Date modificationTime;

    /**
     * 文档数量
     */
    private int documentCount;

    /**
     * 视频数量
     */
    private int videoCount;
}
