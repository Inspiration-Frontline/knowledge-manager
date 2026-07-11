package ifl.agentbreaker.knowledgemanager.domain.dtos.responses;

import ifl.agentbreaker.knowledgemanager.domain.constants.ParsingStatus;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class VideoResponse
{

    /**
     * 视频ID
     */
    private long videoId;

    /**
     * 标题
     */
    private String title;

    /**
     * 标签
     */
    private List<String> tags;

    /**
     * 视频链接
     */
    private String url;

    /**
     * Chunk数量
     */
    private Integer chunkCount;

    /**
     * 解析状态
     */
    private ParsingStatus parsingStatus;

    /**
     * 导入时间
     */
    private Date creationTime;

}
