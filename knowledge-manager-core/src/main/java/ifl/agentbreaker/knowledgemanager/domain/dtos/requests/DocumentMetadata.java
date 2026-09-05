package ifl.agentbreaker.knowledgemanager.domain.dtos.requests;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class DocumentMetadata
{
    /**
     * 原始文件名称
     */
    @NotBlank
    private String fileName;

    /**
     * 文件大小
     */
    @Min(1)
    private long fileSize;

    /**
     * 文件MD5
     */
    @NotBlank
    private String fileMd5;
}
