package ifl.agentbreaker.knowledgemanager.domain.entities.pg;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class DocumentImageChunkBase extends KnowledgeBaseChunkBase
{
    /**
     * 来源文档ID - 关联原始文档
     */
    private long documentId;

    /**
     * 图片宽度 - 图片像素宽度
     */
    private int width;

    /**
     * 图片高度 - 图片像素高度
     */
    private int height;

    /**
     * 所属章节编号 - 标识图片所在文档章节位置
     */
    private String sectionNumber;

    /**
     * 图片描述 - 文档中对该图片的文字描述
     */
    private String description;

    /**
     * 文档图片url - 把文档中的图片截出来后存入oss
     */
    private String nameInOss;
}
