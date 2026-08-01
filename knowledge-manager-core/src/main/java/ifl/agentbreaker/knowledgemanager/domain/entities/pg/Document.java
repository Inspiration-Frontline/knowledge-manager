package ifl.agentbreaker.knowledgemanager.domain.entities.pg;

import ifl.agentbreaker.knowledgemanager.domain.constants.DocumentType;
import ifl.agentbreaker.knowledgemanager.domain.constants.ParsingStatus;
import ifl.agentbreaker.knowledgemanager.domain.constants.SourceType;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.Instant;
import java.util.List;

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
     * 文档类型: PDF; HTML; Markdown; Docx
     */
    private DocumentType type;

    /**
     * 文档标签 - 对文档内容进行分类标记
     */
    private List<String> tags;

    /**
     * 来源类型 - 0 - upload; 1 - crawl; 2 - sync
     */
    private SourceType sourceType;

    /**
     * OSS地址 - 用于存储文件在OSS的位置
     */
    private String nameInOss;

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
    private Instant lastParsingTime;
}
