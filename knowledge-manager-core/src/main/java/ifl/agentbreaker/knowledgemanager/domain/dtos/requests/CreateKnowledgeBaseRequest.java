package ifl.agentbreaker.knowledgemanager.domain.dtos.requests;

import ifl.agentbreaker.knowledgemanager.domain.constants.ChunkType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


@Data
public class CreateKnowledgeBaseRequest
{
    /**
     * 所属业务ID
     */
    private long bizId;

    /**
     * 知识库名称
     */
    @NotBlank
    private String name;

    /**
     * 知识库chunk类型 - 文档 or 图片 or 视频
     */
    @NotNull
    private ChunkType chunkType;

    /**
     * 知识库描述
     */
    private String description;

    /**
     * Embedding模型
     */
    @NotBlank
    private String embeddingModel;

    /**
     * 向量维度数量
     */
    private int embeddingDimensionCount;

    /**
     * Chunk大小
     */
    private Integer minChunkSize;

    /**
     * Chunk重叠长度
     */
    private Integer chunkOverlap;
}
