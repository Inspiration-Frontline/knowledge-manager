package ifl.agentbreaker.knowledgemanager.domain.dtos.requests;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class UploadDocumentsRequest
{
    /**
     * 所属知识库
     */
    private long knowledgeBaseId;

    /**
     * 文件
     */
    @NotNull
    private List<MultipartFile> files;
}