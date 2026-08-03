package ifl.agentbreaker.knowledgemanager.domain.dtos.responses;

import ifl.agentbreaker.knowledgemanager.domain.constants.DocumentType;
import ifl.agentbreaker.knowledgemanager.domain.constants.ParsingStatus;
import ifl.agentbreaker.knowledgemanager.domain.constants.SourceType;
import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
public class DocumentDetail
{

    /**
     * 文档ID
     */
    private long documentId;

    /**
     * 所属知识库
     */
    private long knowledgeBaseId;

    /**
     * 文档名称
     */
    private String name;

    /**
     * 文档类型
     */
    private DocumentType type;

    /**
     * 文档标签 - 对文档内容进行分类标记
     */
    private List<String> tags;

    /**
     * 来源类型
     */
    private SourceType sourceType;

    /**
     * 文件大小(Byte)
     */
    private long fileSize;

    /**
     * 解析状态
     */
    private ParsingStatus parsingStatus;

    /**
     * Chunk数量
     */
    private long chunkCount;

    /**
     * 上传时间
     */
    private Instant creationTime;

    /**
     * 最后一次成功完成解析的时间
     */
    private Instant lastParsingTime;

}
