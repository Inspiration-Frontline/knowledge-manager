package ifl.agentbreaker.knowledgemanager.domain.entities.pg;

import ifl.agentbreaker.knowledgemanager.domain.constants.ParseStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class Video extends EntityBase
{
    /**
     * 知识库ID - 关联知识库
     */
    private long knowledgeBaseId;

    /**
     * 视频标题 - 可生成Embedding
     */
    private String title;

    /**
     * 视频简介 - 可生成Embedding
     */
    private String description;

    /**
     * 视频标签 - 逗号分隔，可生成Embedding
     */
    private List<String> tags;

    /**
     * 视频链接 - 比如B站/Youtube链接
     */
    private String url;

    /**
     * 解析状态 - 0未解析 1成功 2失败
     */
    private ParseStatus parseStatus;

    /**
     * Chunk数量 - 解析后生成了多少chunk
     */
    private Integer chunkCount;

    /**
     * 最后一次成功完成解析的时间 - 方便排查解析问题
     */
    private Date lastParseTime;
}
