package ifl.agentbreaker.knowledgemanager.domain.dtos.responses;

import ifl.agentbreaker.knowledgemanager.domain.constants.DocumentType;
import ifl.agentbreaker.knowledgemanager.domain.constants.ParsingStatus;
import ifl.agentbreaker.knowledgemanager.domain.constants.SourceType;
import lombok.Data;

import java.util.Date;

@Data
public class DocumentResponse
{

    /**
     * 文档ID
     */
    private long documentId;

    /**
     * 文档名称
     */
    private String documentName;

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
     * Chunk数量
     */
    private Integer chunkCount;

    /**
     * 解析状态
     */
    private ParsingStatus parsingStatus;

    /**
     * 上传时间
     */
    private Date creationTime;

}
