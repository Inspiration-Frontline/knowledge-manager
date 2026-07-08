package ifl.agentbreaker.knowledgemanager.controllers;

import ifl.agentbreaker.knowledgemanager.domain.dtos.requests.PageDocumentsRequest;
import ifl.agentbreaker.knowledgemanager.domain.dtos.requests.UploadDocumentRequest;
import ifl.agentbreaker.knowledgemanager.domain.dtos.responses.DocumentDetailResponse;
import ifl.agentbreaker.knowledgemanager.domain.dtos.responses.DocumentResponse;
import ifl.agentbreaker.knowledgemanager.domain.dtos.responses.PageResponse;
import ifl.agentbreaker.knowledgemanager.services.DocumentService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import stark.dataworks.boot.web.ServiceResponse;

@Slf4j
@RestController
@RequestMapping("/document")
public class DocumentController
{
    @Autowired
    private DocumentService service;

    /**
     * 上传文档
     * @param request
     * @return
     */
    @PostMapping("/upload")
    public ServiceResponse<Long> uploadDocument(@ModelAttribute @Valid UploadDocumentRequest request)
    {
        return service.uploadDocument(request);
    }

    /**
     * 删除文档
     * @param documentId
     * @return
     */
    @DeleteMapping("/{documentId}")
    public ServiceResponse<Boolean> deleteDocument(@PathVariable long documentId)
    {
        return service.deleteDocument(documentId);
    }

    /**
     * 查询单个文档详情
     * @param documentId
     * @return
     */
    @GetMapping("/detail/{documentId}")
    public ServiceResponse<DocumentDetailResponse> getDocumentDetail(@PathVariable long documentId)
    {
        return service.getDocumentDetail(documentId);
    }

    /**
     * 获取文档临时下载地址
     * @param documentId
     * @return
     */
    @GetMapping("/download/{documentId}")
    public ServiceResponse<String> getDocumentDownloadUrl(@PathVariable long documentId)
    {
        return service.getDocumentDownloadUrl(documentId);
    }

    /**
     * 分页查询文档列表
     * @param request
     * @return
     */
    @GetMapping("/page")
    public ServiceResponse<PageResponse<DocumentResponse>> pageDocuments(@Valid PageDocumentsRequest request)
    {
        return service.pageDocuments(request);
    }
}
