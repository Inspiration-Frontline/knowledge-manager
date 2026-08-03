package ifl.agentbreaker.knowledgemanager.domain.entities.pg;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class DocumentChunkBase extends KnowledgeBaseChunkBase
{
    /**
     * 来源文档ID - 关联原始文档
     */
    private long documentId;

    /**
     * chunk序号 - 从0开始
     */
    private int chunkNumber;

    /**
     * 所属章节编号 - 标识chunk主要对应的文档章节位置，用于保留原始文档结构信息
     */
    private String sectionNumber;

    /**
     * chunk内容 - 实际文本片段
     */
    private String chunkContent;

    /**
     * 前置chunk摘要 - 当前chunk之前相邻chunk的内容摘要，用于补充上下文信息
     */
    private String previousChunkAbstract;

    /**
     * 后置chunk摘要 - 当前chunk之后相邻chunk的内容摘要，用于补充上下文信息
     */
    private String nextChunkAbstract;

    /**
     * 引用图片chunk ID 列表 - 当前文本chunk关联的文档图片chunk
     */
    private List<Long> referencedImageChunkIds;
}
