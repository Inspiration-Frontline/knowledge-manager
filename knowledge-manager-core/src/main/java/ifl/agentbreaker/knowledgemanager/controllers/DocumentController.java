package ifl.agentbreaker.knowledgemanager.controllers;

import ifl.agentbreaker.knowledgemanager.domain.dtos.requests.PageDocumentChunksRequest;
import ifl.agentbreaker.knowledgemanager.domain.dtos.requests.PageDocumentsRequest;
import ifl.agentbreaker.knowledgemanager.domain.dtos.requests.UploadDocumentsRequest;
import ifl.agentbreaker.knowledgemanager.domain.dtos.responses.*;
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
    private DocumentService documentService;

    /**
     * 上传文档
     * @param request
     * @return
     */
    @PostMapping("/upload")
    public ServiceResponse<Boolean> uploadDocuments(@ModelAttribute @Valid UploadDocumentsRequest request)
    {
        return documentService.uploadDocuments(request);
    }

    /**
     * 删除文档
     * @param documentId
     * @return
     */
    @DeleteMapping("/{documentId}")
    public ServiceResponse<Boolean> deleteDocument(@PathVariable long documentId)
    {
        return documentService.deleteDocument(documentId);
    }

    /**
     * 分页查询文档列表
     * @param request
     * @return
     */
    @GetMapping("/page")
    public ServiceResponse<PageResponse<DocumentResponse>> pageDocuments(@ModelAttribute @Valid PageDocumentsRequest request)
    {
        return documentService.pageDocuments(request);
    }

    /**
     * 查询单个文档详情
     * @param documentId
     * @return
     */
    @GetMapping("/detail/{documentId}")
    public ServiceResponse<DocumentDetailResponse> getDocumentDetail(@PathVariable long documentId)
    {
        return documentService.getDocumentDetail(documentId);
    }

    /**
     * 获取文档临时下载地址
     * @param documentId
     * @return
     */
    @GetMapping("/download/{documentId}")
    public ServiceResponse<String> getDownloadUrl(@PathVariable long documentId)
    {
        return documentService.getDownloadUrl(documentId);
    }

    /**
     * 分页查询文档chunk
     * @param request
     * @return
     */
    @GetMapping("/chunks/page")
    public ServiceResponse<PageResponse<DocumentChunkResponse>> pageDocumentChunks(@ModelAttribute @Valid PageDocumentChunksRequest request)
    {
        return documentService.pageDocumentChunks(request);
    }

    /**
     * 查询文档chunk详情
     * @param chunkId
     * @return
     */
    @GetMapping("/chunk/detail/{chunkId}")
    public ServiceResponse<DocumentChunkDetailResponse> getDocumentChunkDetail(@PathVariable long chunkId)
    {
        return documentService.getDocumentChunkDetail(chunkId);
    }

    /**
     * 删除文档chunk及相关联的文档图片chunk
     * @param chunkId
     * @return
     */
    @DeleteMapping("/chunk/{chunkId}")
    public ServiceResponse<Boolean> deleteDocumentChunk(@PathVariable long chunkId)
    {
        return documentService.deleteDocumentChunk(chunkId);
    }

    // TODO: Need to determine if we need to implement independent CRUD features for document image chunks.
}
