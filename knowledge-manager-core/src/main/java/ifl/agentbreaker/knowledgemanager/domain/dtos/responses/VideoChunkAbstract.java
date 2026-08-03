package ifl.agentbreaker.knowledgemanager.domain.dtos.responses;

import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
public class VideoChunkAbstract
{
    /**
     * 视频Chunk ID
     */
    private long chunkId;

    /**
     * 图片内容摘要 - LLM生成
     */
    private String chunkAbstract;

    /**
     * 视频宽度
     */
    private int width;

    /**
     * 视频高度
     */
    private int height;

    /**
     * 视频标题
     */
    private String title;

    /**
     * 视频简介摘要
     */
    private String introductionAbstract;

    /**
     * 视频标签
     */
    private List<String> tags;

    /**
     * Token数量
     */
    private int tokenCount;

    /**
     * 创建时间
     */
    private Instant creationTime;
}
