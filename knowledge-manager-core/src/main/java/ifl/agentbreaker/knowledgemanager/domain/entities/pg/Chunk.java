package ifl.agentbreaker.knowledgemanager.domain.entities.pg;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class Chunk extends EntityBase
{
    /**
     * 知识库ID - 关联知识库
     */
    private long knowledgeBaseId;

    /**
     * 文档ID - 来源文档（与videoId不同时非空）
     */
    private Long documentId;

    /**
     * 视频ID - 来源视频（与documentId不同时非空）
     */
    private Long videoId;

    /**
     * chunk序号 - 从0开始
     */
    private int chunkNo;

    /**
     * chunk内容 - 实际文本片段
     */
    private String chunkContent;

    /**
     * token数量 - 该chunk有多少token
     */
    private int tokenCount;

    /**
     * 元数据 - 存储上下文信息
     */
    private List<String> metadata;

    /**
     * 向量数据 - pgvector
     */
    private float[] embedding;
}
