package ifl.agentbreaker.knowledgemanager.domain.dtos.responses;

import ifl.agentbreaker.knowledgemanager.domain.constants.ParsingStatus;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class VideoDetailResponse
{

    /**
     * 视频ID
     */
    private long videoId;

    /**
     * 所属知识库
     */
    private long knowledgeBaseId;

    /**
     * 标题
     */
    private String title;

    /**
     * 简介
     */
    private String description;

    /**
     * 标签
     */
    private List<String> tags;

    /**
     * 视频地址
     */
    private String url;

    /**
     * 解析状态
     */
    private ParsingStatus parsingStatus;

    /**
     * Chunk数量
     */
    private Integer chunkCount;

    /**
     * 导入时间
     */
    private Date creationTime;

    /**
     * 最后一次成功完成解析的时间
     */
    private Date lastParseTime;

}
