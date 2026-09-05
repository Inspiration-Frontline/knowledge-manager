package ifl.agentbreaker.knowledgemanager.domain.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class OssCallbackRequest
{
    /**
     * bucket名称
     */
    @NotBlank
    private String bucketName;

    /**
     * 文档在oss中的名称
     */
    @NotBlank
    private String nameInOss;

    /**
     * 文档大小
     */
    private long fileSize;
}
