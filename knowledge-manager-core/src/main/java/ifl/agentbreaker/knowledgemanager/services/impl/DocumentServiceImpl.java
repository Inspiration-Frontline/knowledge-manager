package ifl.agentbreaker.knowledgemanager.services.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import ifl.agentbreaker.knowledgemanager.domain.dtos.requests.PageDocumentChunksRequest;
import ifl.agentbreaker.knowledgemanager.domain.dtos.requests.PageDocumentImageChunksRequest;
import ifl.agentbreaker.knowledgemanager.domain.dtos.requests.PageDocumentsRequest;
import ifl.agentbreaker.knowledgemanager.domain.dtos.requests.UploadDocumentsRequest;
import ifl.agentbreaker.knowledgemanager.domain.dtos.responses.*;
import ifl.agentbreaker.knowledgemanager.domain.entities.pg.Document;
import ifl.agentbreaker.knowledgemanager.mappers.DocumentChunkMapper;
import ifl.agentbreaker.knowledgemanager.mappers.DocumentImageChunkMapper;
import ifl.agentbreaker.knowledgemanager.mappers.DocumentMapper;
import ifl.agentbreaker.knowledgemanager.services.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import stark.dataworks.boot.web.ServiceResponse;

@Service
public class DocumentServiceImpl extends ServiceImpl<DocumentMapper, Document> implements DocumentService
{
    @Autowired
    private DocumentMapper documentMapper;

    @Autowired
    private DocumentChunkMapper documentChunkMapper;

    @Autowired
    private DocumentImageChunkMapper documentImageChunkMapper;

    @Override
    public ServiceResponse<Boolean> uploadDocuments(UploadDocumentsRequest request)
    {
        return null;
    }

    @Override
    public ServiceResponse<Boolean> deleteDocument(long documentId)
    {
        return null;
    }

    @Override
    public ServiceResponse<PageResponse<DocumentAbstract>> pageDocuments(PageDocumentsRequest request)
    {
        return null;
    }

    @Override
    public ServiceResponse<DocumentDetail> getDocumentDetail(long documentId)
    {
        return null;
    }

    @Override
    public ServiceResponse<String> getDownloadUrl(long documentId)
    {
        return null;
    }

    @Override
    public ServiceResponse<PageResponse<DocumentChunkAbstract>> pageDocumentChunks(PageDocumentChunksRequest request)
    {
        return null;
    }

    @Override
    public ServiceResponse<DocumentChunkDetail> getDocumentChunkDetail(long chunkId)
    {
        return null;
    }

    @Override
    public ServiceResponse<PageResponse<DocumentImageChunkAbstract>> pageDocumentImageChunks(PageDocumentImageChunksRequest request)
    {
        return null;
    }

    @Override
    public ServiceResponse<DocumentImageChunkDetail> getDocumentImageChunkDetail(long chunkId)
    {
        return null;
    }

    @Override
    public ServiceResponse<Boolean> deleteDocumentChunk(long chunkId)
    {
        return null;
    }
}
