package ifl.agentbreaker.knowledgemanager.services;

import com.baomidou.mybatisplus.extension.service.IService;
import ifl.agentbreaker.knowledgemanager.domain.dtos.requests.PageDocumentChunksRequest;
import ifl.agentbreaker.knowledgemanager.domain.dtos.requests.PageDocumentsRequest;
import ifl.agentbreaker.knowledgemanager.domain.dtos.requests.UploadDocumentsRequest;
import ifl.agentbreaker.knowledgemanager.domain.dtos.responses.*;
import ifl.agentbreaker.knowledgemanager.domain.entities.pg.Document;
import jakarta.validation.Valid;
import stark.dataworks.boot.web.ServiceResponse;

public interface DocumentService extends IService<Document>
{
    ServiceResponse<Boolean> uploadDocuments(@Valid UploadDocumentsRequest request);

    ServiceResponse<Boolean> deleteDocument(long documentId);

    ServiceResponse<PageResponse<DocumentResponse>> pageDocuments(@Valid PageDocumentsRequest request);

    ServiceResponse<DocumentDetailResponse> getDocumentDetail(long documentId);

    ServiceResponse<String> getDownloadUrl(long documentId);

    ServiceResponse<PageResponse<DocumentChunkResponse>> pageDocumentChunks(@Valid PageDocumentChunksRequest request);

    ServiceResponse<DocumentChunkDetailResponse> getDocumentChunkDetail(long chunkId);

    ServiceResponse<Boolean> deleteDocumentChunk(long chunkId);
}
