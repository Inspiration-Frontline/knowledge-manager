package ifl.agentbreaker.knowledgemanager.domain.entities.pg;

import lombok.Data;
import lombok.EqualsAndHashCode;


// Base class for chunks of knowledge bases.
@Data
@EqualsAndHashCode(callSuper = true)
public class KnowledgeBaseChunkBase extends EntityBase
{
    /**
     * 知识库ID - 关联知识库
     */
    private long knowledgeBaseId;

    /**
     * 分块摘要，即分块内容的文字版简述。
     */
    private String chunkAbstract;

    /**
     * token数量 - 该chunk有多少token
     */
    private int tokenCount;

    /**
     * 向量数据 - pgvector
     */
    private float[] embedding;
}
