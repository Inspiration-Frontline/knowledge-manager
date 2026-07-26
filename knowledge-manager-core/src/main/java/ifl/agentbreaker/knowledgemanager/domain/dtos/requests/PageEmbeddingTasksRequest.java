package ifl.agentbreaker.knowledgemanager.domain.dtos.requests;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class PageEmbeddingTasksRequest extends PageRequest
{

    /**
     * 来源知识库
     */
    private Long knowledgeBaseId;

    /**
     * 文档ID
     */
    private Long documentId;

    /**
     * 视频ID
     */
    private Long videoId;

    /**
     * 任务状态
     */
    private EmbeddingTaskStatus status;

}
