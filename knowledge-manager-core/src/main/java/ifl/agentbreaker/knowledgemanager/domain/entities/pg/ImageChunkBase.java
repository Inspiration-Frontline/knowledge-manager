package ifl.agentbreaker.knowledgemanager.domain.entities.pg;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ImageChunkBase extends KnowledgeBaseChunkBase
{
    /**
     * 图片宽度 - 图片像素宽度
     */
    private int width;

    /**
     * 图片高度 - 图片像素高度
     */
    private int height;

    /**
     * 图片url - 外部图片访问地址
     */
    private String imageUrl;
}
