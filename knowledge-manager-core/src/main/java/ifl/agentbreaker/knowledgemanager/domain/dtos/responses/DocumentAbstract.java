package ifl.agentbreaker.knowledgemanager.domain.dtos.responses;

import ifl.agentbreaker.knowledgemanager.domain.constants.DocumentType;
import ifl.agentbreaker.knowledgemanager.domain.constants.ParsingStatus;
import ifl.agentbreaker.knowledgemanager.domain.constants.SourceType;
import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
public class DocumentAbstract
{

    /**
     * 文档ID
     */
    private long documentId;

    /**
     * 文档名称
     */
    private String name;

    /**
     * 文档类型
     */
    private DocumentType type;

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
     * chunk数量
     */
    private long chunkCount;

    /**
     * 上传时间
     */
    private Instant creationTime;

}
