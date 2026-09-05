package ifl.agentbreaker.knowledgemanager.services.impl;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.OSSObject;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import ifl.agentbreaker.knowledgemanager.config.DocumentUploadProperties;
import ifl.agentbreaker.knowledgemanager.domain.constants.*;
import ifl.agentbreaker.knowledgemanager.domain.dtos.document.ParsedDocument;
import ifl.agentbreaker.knowledgemanager.domain.dtos.requests.*;
import ifl.agentbreaker.knowledgemanager.domain.dtos.responses.*;
import ifl.agentbreaker.knowledgemanager.domain.entities.pg.Document;
import ifl.agentbreaker.knowledgemanager.domain.entities.pg.KnowledgeBaseMetadata;
import ifl.agentbreaker.knowledgemanager.exception.KnowledgeManagerBusinessError;
import ifl.agentbreaker.knowledgemanager.exception.ServiceResponseException;
import ifl.agentbreaker.knowledgemanager.mappers.DocumentChunkMapper;
import ifl.agentbreaker.knowledgemanager.mappers.DocumentImageChunkMapper;
import ifl.agentbreaker.knowledgemanager.mappers.DocumentMapper;
import ifl.agentbreaker.knowledgemanager.mappers.KnowledgeBaseMapper;
import ifl.agentbreaker.knowledgemanager.services.DocumentService;
import ifl.agentbreaker.knowledgemanager.services.OssService;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import stark.dataworks.boot.web.ServiceResponse;

import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class DocumentServiceImpl extends ServiceImpl<DocumentMapper, Document> implements DocumentService
{
    @Autowired
    private DocumentMapper documentMapper;

    @Autowired
    private DocumentChunkMapper documentChunkMapper;

    @Autowired
    private DocumentImageChunkMapper documentImageChunkMapper;

    @Autowired
    private KnowledgeBaseMapper knowledgeBaseMapper;

    @Autowired
    private OssService ossService;

    @Autowired
    private DocumentUploadProperties documentUploadProperties;

    @Override
    @Transactional
    public ServiceResponse<List<UploadDocumentUrl>> uploadDocuments(UploadDocumentsRequest request)
    {
        // Validate request parameters.
        if (request.getDocuments()
                   .size() > documentUploadProperties.getMaxCount())
        {
            return ServiceResponse.buildErrorResponse(
                    KnowledgeManagerBusinessError.DOCUMENT_COUNT_EXCEEDS_LIMIT.getCode(),
                    KnowledgeManagerBusinessError.DOCUMENT_COUNT_EXCEEDS_LIMIT.getMessage()
            );
        }

        if (request.getDocumentKnowledgeBaseId() == request.getDocumentImageKnowledgeBaseId() ||
                request.getDocumentKnowledgeBaseId() <= 0 || request.getDocumentImageKnowledgeBaseId() <= 0)
        {
            return ServiceResponse.buildErrorResponse(
                    KnowledgeManagerBusinessError.ERROR_BAD_REQUEST.getCode(),
                    KnowledgeManagerBusinessError.ERROR_BAD_REQUEST.getMessage()
            );
        }

        // Validate whether two knowledge bases already exist.
        Map<Long, KnowledgeBaseMetadata> knowledgeBaseMap = knowledgeBaseMapper.selectByIds(List.of(request.getDocumentKnowledgeBaseId(), request.getDocumentImageKnowledgeBaseId()))
                                                                               .stream()
                                                                               .collect(Collectors.toMap(KnowledgeBaseMetadata::getId, Function.identity()));
        KnowledgeBaseMetadata documentKnowledgeBase = knowledgeBaseMap.get(request.getDocumentKnowledgeBaseId());
        KnowledgeBaseMetadata documentImageKnowledgeBase = knowledgeBaseMap.get(request.getDocumentImageKnowledgeBaseId());
        if (documentKnowledgeBase == null || documentKnowledgeBase.getChunkType() != ChunkType.DOCUMENT ||
                documentImageKnowledgeBase == null || documentImageKnowledgeBase.getChunkType() != ChunkType.DOCUMENT_IMAGE)
        {
            return ServiceResponse.buildErrorResponse(
                    KnowledgeManagerBusinessError.ERROR_BAD_REQUEST.getCode(),
                    KnowledgeManagerBusinessError.ERROR_BAD_REQUEST.getMessage()
            );
        }

        // Traverse files.
        List<Document> documents = new ArrayList<>();
        List<UploadDocumentUrl> response = new ArrayList<>();
        for (DocumentMetadata metadata : request.getDocuments())
        {
            String name = metadata.getFileName();
            String extension = FilenameUtils.getExtension(name)
                                            .toLowerCase(Locale.ROOT);
            // Validate document type.
            DocumentType type;
            String contentType = "";
            switch (extension)
            {
                case "pdf":
                    type = DocumentType.PDF;
                    contentType = "application/pdf";
                    break;
                case "html":
                    type = DocumentType.HTML;
                    contentType = "text/html";
                    break;
                case "md":
                case "markdown":
                    type = DocumentType.MARKDOWN;
                    contentType = "text/markdown";
                    break;
                case "docx":
                    type = DocumentType.DOCX;
                    contentType = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
                    break;
                default:
                    return ServiceResponse.buildErrorResponse(
                            KnowledgeManagerBusinessError.DOCUMENT_UNSUPPORTED_TYPE.getCode(),
                            KnowledgeManagerBusinessError.DOCUMENT_UNSUPPORTED_TYPE.getMessage()
                    );
            }
            // Validate whether document already exists.
            boolean exists = documentMapper.exists(Wrappers.lambdaQuery(Document.class)
                                                           .eq(Document::getDocumentKnowledgeBaseId, request.getDocumentKnowledgeBaseId())
                                                           .eq(Document::getFileMd5, metadata.getFileMd5()));
            if (exists)
            {
                return ServiceResponse.buildErrorResponse(
                        KnowledgeManagerBusinessError.DOCUMENT_ALREADY_EXISTS.getCode(),
                        KnowledgeManagerBusinessError.DOCUMENT_ALREADY_EXISTS.getMessage()
                );
            }
            // Create pre-signed upload url.
            String nameInOss = "document_" + UUID.randomUUID() + "." + extension;
            String uploadUrl = "";
            try
            {
                uploadUrl = ossService.generateUploadUrl(nameInOss,
                        metadata.getFileSize(),
                        contentType,
                        documentUploadProperties.getUrlExpirationSeconds());
            }
            catch (OSSException | ClientException e)
            {
                return ServiceResponse.buildErrorResponse(
                        KnowledgeManagerBusinessError.OSS_UPLOAD_ERROR.getCode(),
                        KnowledgeManagerBusinessError.OSS_UPLOAD_ERROR.getMessage()
                );
            }

            // Construct document entity.(except for 'chunkCount', 'lastParsingStatus' and 'uploadTime')
            Document document = new Document();
            document.setDocumentKnowledgeBaseId(request.getDocumentKnowledgeBaseId());
            document.setDocumentImageKnowledgeBaseId(request.getDocumentImageKnowledgeBaseId());
            document.setName(name);
            document.setType(type);
            document.setSourceType(SourceType.UPLOAD);
            document.setNameInOss(nameInOss);
            document.setFileSize(metadata.getFileSize());
            document.setFileMd5(metadata.getFileMd5());
            document.setParsingStatus(ParsingStatus.UNPARSED);
            // Waiting OSS callback.
            document.setUploadStatus((UploadStatus.UPLOADING));
            documents.add(document);

            // Construct response.
            UploadDocumentUrl uploadDocumentUrl = new UploadDocumentUrl();
            uploadDocumentUrl.setFileName(name);
            uploadDocumentUrl.setNameInOss(nameInOss);
            uploadDocumentUrl.setUploadUrl(uploadUrl);
            uploadDocumentUrl.setContentType(contentType);
            response.add(uploadDocumentUrl);
        }

        // Batch insert documents.
        documentMapper.insert(documents);

        return ServiceResponse.buildSuccessResponse(response);
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

    @Override
    @Transactional
    public void parseDocument(long documentId)
    {
        // Select the document by documentId.
        Document document = documentMapper.selectById(documentId);
        if (document == null)
        {
            throw new ServiceResponseException(KnowledgeManagerBusinessError.DOCUMENT_NOT_EXISTS);
        }

        // Prevent illegal invocation.
        if (document.getParsingStatus() != ParsingStatus.PARSING)
        {
            throw new ServiceResponseException(KnowledgeManagerBusinessError.DOCUMENT_STATUS_ERROR);
        }

        OSSObject ossObject = null;
        try
        {
            // 1. Obtain the knowledge base configuration.
            Map<Long, KnowledgeBaseMetadata> knowledgeBaseMap = knowledgeBaseMapper.selectByIds(List.of(document.getDocumentKnowledgeBaseId(), document.getDocumentImageKnowledgeBaseId()))
                                                                                   .stream()
                                                                                   .collect(Collectors.toMap(KnowledgeBaseMetadata::getId, Function.identity()));
            KnowledgeBaseMetadata documentKnowledgeBase = knowledgeBaseMap.get(document.getDocumentKnowledgeBaseId());
            KnowledgeBaseMetadata documentImageKnowledgeBase = knowledgeBaseMap.get(document.getDocumentImageKnowledgeBaseId());
            if (documentKnowledgeBase == null || documentImageKnowledgeBase == null)
            {
                throw new ServiceResponseException(KnowledgeManagerBusinessError.KNOWLEDGE_BASE_NOT_EXISTS);
            }

            // 2. Download the original document from OSS.
            ossObject = ossService.download(document.getNameInOss());

            // Parse the document based on the file type.
            // PDF HTML Markdown DOCX
            // Document -> ParsedDocument( ParsedSection ( content + ParsedImage ) )
            ParsedDocument parsedDocument;
//            parsedDocument.setDocumentId(documentId);
            // 根据DocumentType调用对应解析器
            // 提取 文本 + 图片

            // 图片上传OSS 生成文档图片chunk

            // 文本切chunk

            // 保存文本chunk 和 文档图片chunk

            // 生成embedding

            // 更新Document状态
        }
        catch (Exception e)
        {
            documentMapper.update(Wrappers.lambdaUpdate(Document.class)
                                          .set(Document::getParsingStatus, ParsingStatus.FAILED)
                                          .set(Document::getModifierId, -1)
                                          .eq(Document::getId, documentId));
            throw new ServiceResponseException(KnowledgeManagerBusinessError.DOCUMENT_PARSING_ERROR);
        }
    }

    @Override
    @Transactional
    public ServiceResponse<Boolean> handleOssCallback(OssCallbackRequest request)
    {
        // Validate whether document already exists.
        Document document = documentMapper.selectOne(Wrappers.lambdaQuery(Document.class)
                                                             .select(Document::getUploadStatus)
                                                             .eq(Document::getNameInOss, request.getNameInOss()));
        if (document == null)
        {
            return ServiceResponse.buildErrorResponse(
                    KnowledgeManagerBusinessError.DOCUMENT_NOT_EXISTS.getCode(),
                    KnowledgeManagerBusinessError.DOCUMENT_NOT_EXISTS.getMessage()
            );
        }

        // Prevent repeated processing of successful OSS callback.
        if (document.getUploadStatus() == UploadStatus.SUCCESS)
        {
            return ServiceResponse.buildSuccessResponse(true);
        }

        // Validate whether OSS object really exists.
        boolean ossExists = ossService.exists(request.getNameInOss());
        if (!ossExists)
        {
            return ServiceResponse.buildErrorResponse(
                    KnowledgeManagerBusinessError.OSS_UPLOAD_ERROR.getCode(),
                    KnowledgeManagerBusinessError.OSS_UPLOAD_ERROR.getMessage()
            );
        }

        // Update document upload status.
        documentMapper.update(Wrappers.lambdaUpdate(Document.class)
                                      .set(Document::getUploadStatus, UploadStatus.SUCCESS)
                                      .set(Document::getUploadTime, Instant.now())
                                      .set(Document::getModifierId, -1)
                                      .eq(Document::getNameInOss, request.getNameInOss()));


        return ServiceResponse.buildSuccessResponse(true);
    }
}
