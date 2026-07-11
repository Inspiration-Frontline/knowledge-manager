package ifl.agentbreaker.knowledgemanager.domain.entities.pg;

import ifl.agentbreaker.knowledgemanager.domain.constants.DocumentType;
import ifl.agentbreaker.knowledgemanager.domain.constants.ParsingStatus;
import ifl.agentbreaker.knowledgemanager.domain.constants.SourceType;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

// TODO: Unnecessary entity. Should be deleted.
@Data
@EqualsAndHashCode(callSuper = true)
public class Document extends EntityBase
{
    /**
     * 知识库ID - 关联知识库
     */
    private long knowledgeBaseId;

    /**
     * 文档名称 - 文档名称用于识别文档
     */
    private String name;

    /**
     * 文档类型 - 0PDF 1HTML 2Markdown 3Word
     */
    private DocumentType type;

    /**
     * 来源类型 - 0upload 1crawl 2feishu
     */
    private SourceType sourceType;

    /**
     * OSS地址 - 用于存储文件在OSS的位置
     */
    private String ossUrl;

    /**
     * 文件大小 - 字节
     */
    private long fileSize;

    /**
     * 文件MD5 - 判重
     */
    private String fileMd5;

    /**
     * 解析状态 - 0未解析 1成功 2失败
     */
    private ParsingStatus parsingStatus;

    /**
     * Chunk数量 - 解析后生成了多少chunk
     */
    private Integer chunkCount;

    /**
     * 最后一次成功完成解析的时间 - 方便排查解析问题
     */
    private Date lastParseTime;
}
