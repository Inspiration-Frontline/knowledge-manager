package ifl.agentbreaker.knowledgemanager.services;

import com.baomidou.mybatisplus.extension.service.IService;
import ifl.agentbreaker.knowledgemanager.domain.dtos.requests.*;
import ifl.agentbreaker.knowledgemanager.domain.dtos.responses.*;
import ifl.agentbreaker.knowledgemanager.domain.entities.pg.Document;
import jakarta.validation.Valid;
import stark.dataworks.boot.web.ServiceResponse;

import java.util.List;

public interface DocumentService extends IService<Document>
{
    ServiceResponse<List<UploadDocumentUrl>> uploadDocuments(@Valid UploadDocumentsRequest request);

    ServiceResponse<Boolean> deleteDocument(long documentId);

    ServiceResponse<PageResponse<DocumentAbstract>> pageDocuments(@Valid PageDocumentsRequest request);

    ServiceResponse<DocumentDetail> getDocumentDetail(long documentId);

    ServiceResponse<String> getDownloadUrl(long documentId);

    ServiceResponse<PageResponse<DocumentChunkAbstract>> pageDocumentChunks(@Valid PageDocumentChunksRequest request);

    ServiceResponse<DocumentChunkDetail> getDocumentChunkDetail(long chunkId);

    ServiceResponse<PageResponse<DocumentImageChunkAbstract>> pageDocumentImageChunks(@Valid PageDocumentImageChunksRequest request);

    ServiceResponse<DocumentImageChunkDetail> getDocumentImageChunkDetail(long chunkId);

    ServiceResponse<Boolean> deleteDocumentChunk(long chunkId);

    void parseDocument(long documentId);

    ServiceResponse<Boolean> handleOssCallback(OssCallbackRequest request);
}
