package ifl.agentbreaker.knowledgemanager.domain.entities.pg;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class VideoChunkBase extends KnowledgeBaseChunkBase
{
    /**
     * 视频url - 视频来源地址，例如B站、Youtube等视频链接
     */
    private String videoUrl;

    /**
     * 视频宽度 - 视频画面像素宽度
     */
    private int width;

    /**
     * 视频高度 - 视频画面像素高度
     */
    private int height;

    /**
     * 视频标题 - 视频名称，用于生成embedding增强检索
     */
    private String title;

    /**
     * 视频简介 - 视频描述信息，用于生成embedding增强检索
     */
    private String introduction;

    /**
     * 视频标签 - 视频分类标签，用于生成embedding增强检索
     */
    private List<String> tags;
}
