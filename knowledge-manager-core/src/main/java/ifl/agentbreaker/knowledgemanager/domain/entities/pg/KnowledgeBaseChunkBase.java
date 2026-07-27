package ifl.agentbreaker.knowledgemanager.domain.entities.pg;

import ifl.agentbreaker.knowledgemanager.domain.constants.ChunkType;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

// Base class for chunks of knowledge bases.
@Data
@EqualsAndHashCode(callSuper = true)
public class KnowledgeBaseChunkBase extends EntityBase
{
    /**
     * 知识库ID - 关联知识库
     */
    private long knowledgeBaseId;

    // Type
    /**
     * chunk来源类型
     */
    private ChunkType chunkType;

//    /**
//     * 文档ID - 来源文档（与videoId不同时非空）
//     */
//    private Long documentId;
//
//    /**
//     * 视频ID - 来源视频（与documentId不同时非空）
//     */
//    private Long videoId;
    // TODO: 视频不切成chunk 直接把视频的元数据写进Chunk表里

    /**
     * chunk序号 - 从0开始
     */
    private int chunkNumber;

    /**
     * chunk内容 - 实际文本片段
     */
    private String chunkContent;

    /**
     * token数量 - 该chunk有多少token
     */
    private int tokenCount;

    /**
     * 向量数据 - pgvector
     */
    private float[] embedding;
}
