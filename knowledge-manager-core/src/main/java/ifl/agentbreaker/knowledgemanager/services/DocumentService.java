package ifl.agentbreaker.knowledgemanager.services;

import com.baomidou.mybatisplus.extension.service.IService;
import ifl.agentbreaker.knowledgemanager.domain.dtos.requests.PageDocumentChunksRequest;
import ifl.agentbreaker.knowledgemanager.domain.dtos.requests.PageDocumentImageChunksRequest;
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

    ServiceResponse<PageResponse<DocumentAbstract>> pageDocuments(@Valid PageDocumentsRequest request);

    ServiceResponse<DocumentDetail> getDocumentDetail(long documentId);

    ServiceResponse<String> getDownloadUrl(long documentId);

    ServiceResponse<PageResponse<DocumentChunkAbstract>> pageDocumentChunks(@Valid PageDocumentChunksRequest request);

    ServiceResponse<DocumentChunkDetail> getDocumentChunkDetail(long chunkId);

    ServiceResponse<PageResponse<DocumentImageChunkAbstract>> pageDocumentImageChunks(@Valid PageDocumentImageChunksRequest request);

    ServiceResponse<DocumentImageChunkDetail> getDocumentImageChunkDetail(long chunkId);

    ServiceResponse<Boolean> deleteDocumentChunk(long chunkId);
}
