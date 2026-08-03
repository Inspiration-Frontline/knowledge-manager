package ifl.agentbreaker.knowledgemanager.domain.dtos.responses;

import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
public class VideoChunkDetail
{
    /**
     * 视频Chunk ID
     */
    private long chunkId;

    /**
     * 视频知识库ID
     */
    private long knowledgeBaseId;

    /**
     * 视频URL
     */
    private String videoUrl;

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
     * 视频简介
     */
    private String introduction;

    /**
     * 视频标签
     */
    private List<String> tags;

    /**
     * chunk摘要 - LLM生成
     */
    private String chunkAbstract;

    /**
     * Token数量
     */
    private int tokenCount;

    /**
     * 创建时间
     */
    private Instant creationTime;
}
