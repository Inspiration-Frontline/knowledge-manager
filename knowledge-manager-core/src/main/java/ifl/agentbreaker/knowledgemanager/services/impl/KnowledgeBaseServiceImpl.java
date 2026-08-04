package ifl.agentbreaker.knowledgemanager.services.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import ifl.agentbreaker.knowledgemanager.domain.constants.ChunkType;
import ifl.agentbreaker.knowledgemanager.domain.dtos.requests.*;
import ifl.agentbreaker.knowledgemanager.domain.dtos.responses.KnowledgeBaseDetail;
import ifl.agentbreaker.knowledgemanager.domain.dtos.responses.KnowledgeBaseAbstract;
import ifl.agentbreaker.knowledgemanager.domain.dtos.responses.PageResponse;
import ifl.agentbreaker.knowledgemanager.domain.entities.pg.KnowledgeBaseMetadata;
import ifl.agentbreaker.knowledgemanager.exception.KnowledgeManagerBusinessError;
import ifl.agentbreaker.knowledgemanager.mappers.KnowledgeBaseMapper;
import ifl.agentbreaker.knowledgemanager.services.DocumentService;
import ifl.agentbreaker.knowledgemanager.services.ImageChunkService;
import ifl.agentbreaker.knowledgemanager.services.KnowledgeBaseService;
import ifl.agentbreaker.knowledgemanager.services.VideoChunkService;
import ifl.agentbreaker.knowledgemanager.utils.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import stark.dataworks.boot.web.ServiceResponse;

import java.util.List;
import java.util.Locale;
import java.util.Objects;

@Service
public class KnowledgeBaseServiceImpl extends ServiceImpl<KnowledgeBaseMapper, KnowledgeBaseMetadata> implements KnowledgeBaseService
{
    @Autowired
    private KnowledgeBaseMapper knowledgeBaseMapper;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private DocumentService documentService;

    @Autowired
    private ImageChunkService imageChunkService;

    @Autowired
    private VideoChunkService videoChunkService;

    @Override
    @Transactional
    public ServiceResponse<Boolean> createKnowledgeBase(CreateKnowledgeBaseRequest request)
    {
        // Validate request parameters.
        if (request.getBizId() <= 0 ||
                request.getEmbeddingDimensionCount() <= 0 ||
                request.getChunkType() == ChunkType.DOCUMENT_IMAGE)
        {
            return ServiceResponse.buildErrorResponse(
                    KnowledgeManagerBusinessError.ERROR_BAD_REQUEST.getCode(),
                    KnowledgeManagerBusinessError.ERROR_BAD_REQUEST.getMessage());
        }
        if (request.getChunkType() == ChunkType.DOCUMENT &&
                (request.getMinChunkSize() == null
                        || request.getChunkOverlap() == null
                        || request.getDocumentImageEmbeddingModel() == null
                        || request.getDocumentImageEmbeddingModel()
                                  .isBlank()
                        || request.getDocumentImageEmbeddingDimensionCount() == null
                        || request.getDocumentImageEmbeddingDimensionCount() <= 0))
        {
            return ServiceResponse.buildErrorResponse(
                    KnowledgeManagerBusinessError.ERROR_BAD_REQUEST.getCode(),
                    KnowledgeManagerBusinessError.ERROR_BAD_REQUEST.getMessage()
            );
        }

        // Validate whether it already exists.
        Long count = knowledgeBaseMapper.selectCount(Wrappers.lambdaQuery(KnowledgeBaseMetadata.class)
                                                             .eq(KnowledgeBaseMetadata::getBizId, request.getBizId())
                                                             .eq(KnowledgeBaseMetadata::getName, request.getName()
                                                                                                        .strip()
                                                                                                        .toLowerCase(Locale.ROOT))
                                                             .eq(KnowledgeBaseMetadata::getChunkType, request.getChunkType()));
        if (count > 0)
        {
            return ServiceResponse.buildErrorResponse(
                    KnowledgeManagerBusinessError.KNOWLEDGE_BASE_ALREADY_EXISTS.getCode(),
                    KnowledgeManagerBusinessError.KNOWLEDGE_BASE_ALREADY_EXISTS.getMessage());
        }

        // Insert knowledge base metadata into database.
        long userId = UserContext.getCurrentUserId();
        KnowledgeBaseMetadata knowledgeBaseMetadata = new KnowledgeBaseMetadata();
        knowledgeBaseMetadata.setCreatorId(userId);
        knowledgeBaseMetadata.setModifierId(userId);
        knowledgeBaseMetadata.setBizId(request.getBizId());
        knowledgeBaseMetadata.setName(request.getName()
                                             .strip()
                                             .toLowerCase(Locale.ROOT));
        knowledgeBaseMetadata.setChunkType(request.getChunkType());
        knowledgeBaseMetadata.setDescription(request.getDescription());
        knowledgeBaseMetadata.setEmbeddingModel(request.getEmbeddingModel()
                                                       .strip());
        knowledgeBaseMetadata.setEmbeddingDimensionCount(request.getEmbeddingDimensionCount());
        knowledgeBaseMetadata.setEnabled(true);
        knowledgeBaseMetadata.setMinChunkSize(request.getMinChunkSize());
        knowledgeBaseMetadata.setChunkOverlap(request.getChunkOverlap());
        knowledgeBaseMapper.insert(knowledgeBaseMetadata);

        // Dynamically create chunk table accordingly.
        createChunkTable(request.getChunkType(),
                buildChunkTableName(request.getChunkType(), request.getName()
                                                                   .strip()
                                                                   .toLowerCase(Locale.ROOT), request.getBizId()),
                request.getEmbeddingDimensionCount());

        // Insert document image knowledge base metadata into database.
        if (request.getChunkType() == ChunkType.DOCUMENT)
        {
            KnowledgeBaseMetadata documentImageKnowledgeBaseMetadata = new KnowledgeBaseMetadata();
            documentImageKnowledgeBaseMetadata.setCreatorId(userId);
            documentImageKnowledgeBaseMetadata.setModifierId(userId);
            documentImageKnowledgeBaseMetadata.setBizId(request.getBizId());
            documentImageKnowledgeBaseMetadata.setName(request.getName()
                                                              .strip()
                                                              .toLowerCase());
            documentImageKnowledgeBaseMetadata.setChunkType(ChunkType.DOCUMENT_IMAGE);
            documentImageKnowledgeBaseMetadata.setDescription(request.getDescription());
            documentImageKnowledgeBaseMetadata.setEmbeddingModel(request.getDocumentImageEmbeddingModel()
                                                                        .strip());
            documentImageKnowledgeBaseMetadata.setEmbeddingDimensionCount(request.getDocumentImageEmbeddingDimensionCount());
            documentImageKnowledgeBaseMetadata.setEnabled(true);
            knowledgeBaseMapper.insert(documentImageKnowledgeBaseMetadata);

            // Dynamically create document image chunk table if chunk type is document.
            createChunkTable(ChunkType.DOCUMENT_IMAGE,
                    buildChunkTableName(ChunkType.DOCUMENT_IMAGE, request.getName()
                                                                         .strip()
                                                                         .toLowerCase(Locale.ROOT), request.getBizId()),
                    request.getDocumentImageEmbeddingDimensionCount());
        }

        return ServiceResponse.buildSuccessResponse(true);
    }

    @Override
    @Transactional
    public ServiceResponse<Boolean> updateKnowledgeBase(UpdateKnowledgeBaseRequest request)
    {
        // Validate request parameters.
        if (request.getBizId() == null &&
                request.getName() == null &&
                request.getDescription() == null &&
                request.getEmbeddingModel() == null &&
                request.getEmbeddingDimensionCount() == null)
        {
            return ServiceResponse.buildErrorResponse(
                    KnowledgeManagerBusinessError.ERROR_BAD_REQUEST.getCode(),
                    KnowledgeManagerBusinessError.ERROR_BAD_REQUEST.getMessage()
            );
        }
        if ((request.getBizId() != null && request.getBizId() <= 0) ||
                (request.getName() != null && request.getName()
                                                     .isBlank()) ||
                (request.getDescription() != null && request.getDescription()
                                                            .isBlank()) ||
                (request.getEmbeddingModel() != null && request.getEmbeddingModel()
                                                               .isBlank()) ||
                (request.getEmbeddingDimensionCount() != null && request.getEmbeddingDimensionCount() <= 0))
        {
            return ServiceResponse.buildErrorResponse(
                    KnowledgeManagerBusinessError.ERROR_BAD_REQUEST.getCode(),
                    KnowledgeManagerBusinessError.ERROR_BAD_REQUEST.getMessage()
            );
        }

        // Validate whether it already exists.
        KnowledgeBaseMetadata knowledgeBaseMetadata = knowledgeBaseMapper.selectById(request.getKnowledgeBaseId());
        if (knowledgeBaseMetadata == null)
        {
            return ServiceResponse.buildErrorResponse(
                    KnowledgeManagerBusinessError.KNOWLEDGE_BASE_NOT_EXISTS.getCode(),
                    KnowledgeManagerBusinessError.KNOWLEDGE_BASE_NOT_EXISTS.getMessage()
            );
        }

        // Validate request parameters.
        ChunkType chunkType = knowledgeBaseMetadata.getChunkType();
        if (chunkType == ChunkType.DOCUMENT || chunkType == ChunkType.DOCUMENT_IMAGE)
        {
            return ServiceResponse.buildErrorResponse(
                    KnowledgeManagerBusinessError.ERROR_BAD_REQUEST.getCode(),
                    KnowledgeManagerBusinessError.ERROR_BAD_REQUEST.getMessage()
            );
        }

        // Construct knowledge base metadata.
        KnowledgeBaseMetadata newKnowledgeBaseMetadata = new KnowledgeBaseMetadata();
        newKnowledgeBaseMetadata.setId(request.getKnowledgeBaseId());
        if (request.getBizId() != null)
            newKnowledgeBaseMetadata.setBizId(request.getBizId());
        else
            newKnowledgeBaseMetadata.setBizId(knowledgeBaseMetadata.getBizId());
        if (request.getName() != null)
            newKnowledgeBaseMetadata.setName(request.getName()
                                                    .strip()
                                                    .toLowerCase(Locale.ROOT));
        else
            newKnowledgeBaseMetadata.setName(knowledgeBaseMetadata.getName());

        if (newKnowledgeBaseMetadata.getBizId() != knowledgeBaseMetadata.getBizId() ||
                !Objects.equals(newKnowledgeBaseMetadata.getName(), knowledgeBaseMetadata.getName()))
        {
            // Validate new chunk table name is not existing.
            Long count = knowledgeBaseMapper.selectCount(Wrappers.lambdaQuery(KnowledgeBaseMetadata.class)
                                                                 .eq(KnowledgeBaseMetadata::getChunkType, knowledgeBaseMetadata.getChunkType())
                                                                 .eq(KnowledgeBaseMetadata::getName, newKnowledgeBaseMetadata.getName())
                                                                 .eq(KnowledgeBaseMetadata::getBizId, newKnowledgeBaseMetadata.getBizId())
                                                                 .ne(KnowledgeBaseMetadata::getId, newKnowledgeBaseMetadata.getId()));
            if (count > 0)
            {
                return ServiceResponse.buildErrorResponse(
                        KnowledgeManagerBusinessError.KNOWLEDGE_BASE_ALREADY_EXISTS.getCode(),
                        KnowledgeManagerBusinessError.KNOWLEDGE_BASE_ALREADY_EXISTS.getMessage()
                );
            }

            // If 'bizId' or 'name' changes, rename related chunk tables accordingly.
            renameChunkTable(buildChunkTableName(knowledgeBaseMetadata.getChunkType(), knowledgeBaseMetadata.getName(), knowledgeBaseMetadata.getBizId()),
                    buildChunkTableName(knowledgeBaseMetadata.getChunkType(), newKnowledgeBaseMetadata.getName(), newKnowledgeBaseMetadata.getBizId()));
        }

        newKnowledgeBaseMetadata.setDescription(request.getDescription());
        if (request.getEmbeddingModel() != null)
            newKnowledgeBaseMetadata.setEmbeddingModel(request.getEmbeddingModel()
                                                              .strip());
        if (request.getEmbeddingDimensionCount() != null)
            newKnowledgeBaseMetadata.setEmbeddingDimensionCount(request.getEmbeddingDimensionCount());
        else
            newKnowledgeBaseMetadata.setEmbeddingDimensionCount(knowledgeBaseMetadata.getEmbeddingDimensionCount());
        newKnowledgeBaseMetadata.setModifierId(UserContext.getCurrentUserId());

        // If 'embeddingModel' or 'embeddingDimensionCount' changes, regenerate each chunk's 'embedding'.
        if ((request.getEmbeddingModel() != null && !Objects.equals(request.getEmbeddingModel()
                                                                           .strip(), knowledgeBaseMetadata.getEmbeddingModel()))
                || !Objects.equals(newKnowledgeBaseMetadata.getEmbeddingDimensionCount(), knowledgeBaseMetadata.getEmbeddingDimensionCount()))
        {
            switch (knowledgeBaseMetadata.getChunkType())
            {
                // TODO
                case IMAGE -> imageChunkService.regenerateEmbedding(request.getKnowledgeBaseId());
                case VIDEO -> videoChunkService.regenerateEmbedding(request.getKnowledgeBaseId());
            }
        }

        // Update knowledge base metadata.
        knowledgeBaseMapper.updateById(newKnowledgeBaseMetadata);

        return ServiceResponse.buildSuccessResponse(true);
    }

    @Override
    @Transactional
    public ServiceResponse<Boolean> updateDocumentKnowledgeBase(UpdateDocumentKnowledgeBaseRequest request)
    {
        // Validate request parameters.
        if (request.getBizId() == null &&
                request.getName() == null &&
                request.getDescription() == null &&
                request.getEmbeddingModel() == null &&
                request.getEmbeddingDimensionCount() == null &&
                request.getMinChunkSize() == null &&
                request.getChunkOverlap() == null)
        {
            return ServiceResponse.buildErrorResponse(
                    KnowledgeManagerBusinessError.ERROR_BAD_REQUEST.getCode(),
                    KnowledgeManagerBusinessError.ERROR_BAD_REQUEST.getMessage()
            );
        }
        if ((request.getBizId() != null && request.getBizId() <= 0) ||
                (request.getName() != null && request.getName()
                                                     .isBlank()) ||
                (request.getDescription() != null && request.getDescription()
                                                            .isBlank()) ||
                (request.getEmbeddingModel() != null && request.getEmbeddingModel()
                                                               .isBlank()) ||
                (request.getEmbeddingDimensionCount() != null && request.getEmbeddingDimensionCount() <= 0) ||
                (request.getMinChunkSize() != null && request.getMinChunkSize() <= 0) ||
                (request.getChunkOverlap() != null && request.getChunkOverlap() <= 0))
        {
            return ServiceResponse.buildErrorResponse(
                    KnowledgeManagerBusinessError.ERROR_BAD_REQUEST.getCode(),
                    KnowledgeManagerBusinessError.ERROR_BAD_REQUEST.getMessage()
            );
        }

        // Validate whether it already exists.
        KnowledgeBaseMetadata knowledgeBaseMetadata = knowledgeBaseMapper.selectById(request.getKnowledgeBaseId());
        if (knowledgeBaseMetadata == null)
        {
            return ServiceResponse.buildErrorResponse(
                    KnowledgeManagerBusinessError.KNOWLEDGE_BASE_NOT_EXISTS.getCode(),
                    KnowledgeManagerBusinessError.KNOWLEDGE_BASE_NOT_EXISTS.getMessage()
            );
        }

        // Validate request parameters.
        ChunkType chunkType = knowledgeBaseMetadata.getChunkType();
        if (chunkType != ChunkType.DOCUMENT)
        {
            return ServiceResponse.buildErrorResponse(
                    KnowledgeManagerBusinessError.ERROR_BAD_REQUEST.getCode(),
                    KnowledgeManagerBusinessError.ERROR_BAD_REQUEST.getMessage()
            );
        }

        // Construct knowledge base metadata.
        // (Document image knowledge base follows document knowledge base)
        KnowledgeBaseMetadata documentImageKnowledgeBaseMetadata = knowledgeBaseMapper.selectOne(Wrappers.lambdaQuery(KnowledgeBaseMetadata.class)
                                                                                                         .eq(KnowledgeBaseMetadata::getBizId, knowledgeBaseMetadata.getBizId())
                                                                                                         .eq(KnowledgeBaseMetadata::getName, knowledgeBaseMetadata.getName())
                                                                                                         .eq(KnowledgeBaseMetadata::getChunkType, ChunkType.DOCUMENT_IMAGE));
        KnowledgeBaseMetadata newKnowledgeBaseMetadata = new KnowledgeBaseMetadata();
        KnowledgeBaseMetadata newDocumentImageKnowledgeBaseMetadata = new KnowledgeBaseMetadata();
        newKnowledgeBaseMetadata.setId(request.getKnowledgeBaseId());
        newDocumentImageKnowledgeBaseMetadata.setId(documentImageKnowledgeBaseMetadata.getId());
        if (request.getBizId() != null)
        {
            newKnowledgeBaseMetadata.setBizId(request.getBizId());
            newDocumentImageKnowledgeBaseMetadata.setBizId(request.getBizId());
        }
        else
        {
            newKnowledgeBaseMetadata.setBizId(knowledgeBaseMetadata.getBizId());
            newDocumentImageKnowledgeBaseMetadata.setBizId(documentImageKnowledgeBaseMetadata.getBizId());
        }
        if (request.getName() != null)
        {
            newKnowledgeBaseMetadata.setName(request.getName()
                                                    .strip()
                                                    .toLowerCase(Locale.ROOT));
            newDocumentImageKnowledgeBaseMetadata.setName(request.getName()
                                                                 .strip()
                                                                 .toLowerCase(Locale.ROOT));
        }
        else
        {
            newKnowledgeBaseMetadata.setName(knowledgeBaseMetadata.getName());
            newDocumentImageKnowledgeBaseMetadata.setName(documentImageKnowledgeBaseMetadata.getName());
        }
        newKnowledgeBaseMetadata.setDescription(request.getDescription());
        newDocumentImageKnowledgeBaseMetadata.setDescription(request.getDescription());
        if (request.getEmbeddingModel() != null)
            newKnowledgeBaseMetadata.setEmbeddingModel(request.getEmbeddingModel()
                                                              .strip());
        if (request.getEmbeddingDimensionCount() != null)
            newKnowledgeBaseMetadata.setEmbeddingDimensionCount(request.getEmbeddingDimensionCount());
        else
            newKnowledgeBaseMetadata.setEmbeddingDimensionCount(knowledgeBaseMetadata.getEmbeddingDimensionCount());
        newKnowledgeBaseMetadata.setMinChunkSize(request.getMinChunkSize());
        newKnowledgeBaseMetadata.setChunkOverlap(request.getChunkOverlap());
        newKnowledgeBaseMetadata.setModifierId(UserContext.getCurrentUserId());
        newDocumentImageKnowledgeBaseMetadata.setModifierId(UserContext.getCurrentUserId());

        // Firstly consider rebuilding chunk table if 'minChunkSize' or 'chunkOverlap' changes.
        // If 'minChunkSize' or 'chunkOverlap' changes,
        // create new table to 'xx_new' →
        // reparse documents and convert chunk data →
        // rename old table to 'xx_old' →
        // rename new table to 'xx' →
        // drop old table.
        // (Only document knowledge base can change these two fields)
        if ((request.getMinChunkSize() != null && !Objects.equals(request.getMinChunkSize(), knowledgeBaseMetadata.getMinChunkSize())) ||
                (request.getChunkOverlap() != null && !Objects.equals(request.getChunkOverlap(), knowledgeBaseMetadata.getChunkOverlap())))
        {
            // TODO
            documentService.rebuildChunkTable(request.getKnowledgeBaseId(), newKnowledgeBaseMetadata);
        }
        // Rename chunk table or regenerate embedding.
        else
        {
            // Validate new chunk table name is not existing.
            if (newKnowledgeBaseMetadata.getBizId() != knowledgeBaseMetadata.getBizId() ||
                    !Objects.equals(newKnowledgeBaseMetadata.getName(), knowledgeBaseMetadata.getName()))
            {
                Long count = knowledgeBaseMapper.selectCount(Wrappers.lambdaQuery(KnowledgeBaseMetadata.class)
                                                                     .eq(KnowledgeBaseMetadata::getChunkType, knowledgeBaseMetadata.getChunkType())
                                                                     .eq(KnowledgeBaseMetadata::getName, newKnowledgeBaseMetadata.getName())
                                                                     .eq(KnowledgeBaseMetadata::getBizId, newKnowledgeBaseMetadata.getBizId())
                                                                     .ne(KnowledgeBaseMetadata::getId, newKnowledgeBaseMetadata.getId()));
                if (count > 0)
                {
                    return ServiceResponse.buildErrorResponse(
                            KnowledgeManagerBusinessError.KNOWLEDGE_BASE_ALREADY_EXISTS.getCode(),
                            KnowledgeManagerBusinessError.KNOWLEDGE_BASE_ALREADY_EXISTS.getMessage()
                    );
                }

                // If 'bizId' or 'name' changes, rename related chunk tables accordingly.
                renameChunkTable(buildChunkTableName(knowledgeBaseMetadata.getChunkType(), knowledgeBaseMetadata.getName(), knowledgeBaseMetadata.getBizId()),
                        buildChunkTableName(knowledgeBaseMetadata.getChunkType(), newKnowledgeBaseMetadata.getName(), newKnowledgeBaseMetadata.getBizId()));
                // (Document image knowledge base follows document knowledge base)
                renameChunkTable(buildChunkTableName(ChunkType.DOCUMENT_IMAGE, knowledgeBaseMetadata.getName(), knowledgeBaseMetadata.getBizId()),
                        buildChunkTableName(ChunkType.DOCUMENT_IMAGE, newKnowledgeBaseMetadata.getName(), newKnowledgeBaseMetadata.getBizId()));
            }

            // If 'embeddingModel' or 'embeddingDimensionCount' changes, regenerate each chunk's 'embedding'.
            if ((request.getEmbeddingModel() != null && !Objects.equals(request.getEmbeddingModel()
                                                                               .strip(), knowledgeBaseMetadata.getEmbeddingModel()))
                    || !Objects.equals(newKnowledgeBaseMetadata.getEmbeddingDimensionCount(), knowledgeBaseMetadata.getEmbeddingDimensionCount()))
            {
                // TODO
                documentService.regenerateDocumentEmbedding(request.getKnowledgeBaseId());
                // (Document image knowledge base follows document knowledge base)
                // TODO
                documentService.regenerateDocumentImageEmbedding(documentImageKnowledgeBaseMetadata.getId());
            }
        }

        // Update knowledge base metadata.
        knowledgeBaseMapper.updateById(newKnowledgeBaseMetadata);
        knowledgeBaseMapper.updateById(newDocumentImageKnowledgeBaseMetadata);

        return ServiceResponse.buildSuccessResponse(true);
    }

    @Override
    @Transactional
    public ServiceResponse<Boolean> updateDocumentImageKnowledgeBase(UpdateDocumentImageKnowledgeBaseRequest request)
    {
        // Validate request parameters.
        if (request.getEmbeddingModel() == null && request.getEmbeddingDimensionCount() == null)
        {
            return ServiceResponse.buildErrorResponse(
                    KnowledgeManagerBusinessError.ERROR_BAD_REQUEST.getCode(),
                    KnowledgeManagerBusinessError.ERROR_BAD_REQUEST.getMessage()
            );
        }
        if ((request.getEmbeddingModel() != null && request.getEmbeddingModel()
                                                           .isBlank())
                || (request.getEmbeddingDimensionCount() != null && request.getEmbeddingDimensionCount() <= 0))
        {
            return ServiceResponse.buildErrorResponse(
                    KnowledgeManagerBusinessError.ERROR_BAD_REQUEST.getCode(),
                    KnowledgeManagerBusinessError.ERROR_BAD_REQUEST.getMessage()
            );
        }

        // Validate whether it already exists.
        KnowledgeBaseMetadata knowledgeBaseMetadata = knowledgeBaseMapper.selectById(request.getKnowledgeBaseId());
        if (knowledgeBaseMetadata == null)
        {
            return ServiceResponse.buildErrorResponse(
                    KnowledgeManagerBusinessError.KNOWLEDGE_BASE_NOT_EXISTS.getCode(),
                    KnowledgeManagerBusinessError.KNOWLEDGE_BASE_NOT_EXISTS.getMessage()
            );
        }

        // Validate request parameters.
        ChunkType chunkType = knowledgeBaseMetadata.getChunkType();
        if (chunkType != ChunkType.DOCUMENT_IMAGE)
        {
            return ServiceResponse.buildErrorResponse(
                    KnowledgeManagerBusinessError.ERROR_BAD_REQUEST.getCode(),
                    KnowledgeManagerBusinessError.ERROR_BAD_REQUEST.getMessage()
            );
        }

        // Construct knowledge base metadata.
        KnowledgeBaseMetadata newKnowledgeBaseMetadata = new KnowledgeBaseMetadata();
        newKnowledgeBaseMetadata.setId(request.getKnowledgeBaseId());
        if (request.getEmbeddingModel() != null)
            newKnowledgeBaseMetadata.setEmbeddingModel(request.getEmbeddingModel()
                                                              .strip());
        if (request.getEmbeddingDimensionCount() != null)
            newKnowledgeBaseMetadata.setEmbeddingDimensionCount(request.getEmbeddingDimensionCount());
        else
            newKnowledgeBaseMetadata.setEmbeddingDimensionCount(knowledgeBaseMetadata.getEmbeddingDimensionCount());
        newKnowledgeBaseMetadata.setModifierId(UserContext.getCurrentUserId());

        // If 'embeddingModel' or 'embeddingDimensionCount' changes, regenerate each chunk's 'embedding'.
        // TODO
        documentService.regenerateDocumentImageEmbedding(request.getKnowledgeBaseId());

        // Update knowledge base metadata.
        knowledgeBaseMapper.updateById(newKnowledgeBaseMetadata);

        return ServiceResponse.buildSuccessResponse(true);
    }

    @Override
    @Transactional
    public ServiceResponse<Boolean> deleteKnowledgeBase(long knowledgeBaseId)
    {
        // Validate whether it already exists.
        KnowledgeBaseMetadata knowledgeBaseMetadata = knowledgeBaseMapper.selectById(knowledgeBaseId);
        if (knowledgeBaseMetadata == null)
        {
            return ServiceResponse.buildErrorResponse(
                    KnowledgeManagerBusinessError.KNOWLEDGE_BASE_NOT_EXISTS.getCode(),
                    KnowledgeManagerBusinessError.KNOWLEDGE_BASE_NOT_EXISTS.getMessage()
            );
        }

        // Validate request parameters.
        if (knowledgeBaseMetadata.getChunkType() == ChunkType.DOCUMENT_IMAGE)
        {
            return ServiceResponse.buildErrorResponse(
                    KnowledgeManagerBusinessError.ERROR_BAD_REQUEST.getCode(),
                    KnowledgeManagerBusinessError.ERROR_BAD_REQUEST.getMessage()
            );
        }

        // Delete knowledge base metadata.
        knowledgeBaseMapper.deleteById(knowledgeBaseId);

        // Drop chunk table accordingly.
        dropChunkTable(buildChunkTableName(knowledgeBaseMetadata.getChunkType(), knowledgeBaseMetadata.getName(), knowledgeBaseMetadata.getBizId()));

        // Delete document image knowledge base metadata.
        if (knowledgeBaseMetadata.getChunkType() == ChunkType.DOCUMENT)
        {
            KnowledgeBaseMetadata documentImageKnowledgeBaseMetadata = knowledgeBaseMapper.selectOne(Wrappers.lambdaQuery(KnowledgeBaseMetadata.class)
                                                                                                             .eq(KnowledgeBaseMetadata::getBizId, knowledgeBaseMetadata.getBizId())
                                                                                                             .eq(KnowledgeBaseMetadata::getName, knowledgeBaseMetadata.getName())
                                                                                                             .eq(KnowledgeBaseMetadata::getChunkType, ChunkType.DOCUMENT_IMAGE));
            knowledgeBaseMapper.deleteById(documentImageKnowledgeBaseMetadata);

            // Drop document image chunk table if chunk type is document.
            dropChunkTable(buildChunkTableName(ChunkType.DOCUMENT_IMAGE, documentImageKnowledgeBaseMetadata.getName(), documentImageKnowledgeBaseMetadata.getBizId()));
        }


        return ServiceResponse.buildSuccessResponse(true);
    }

    @Override
    @Transactional
    public ServiceResponse<Boolean> updateKnowledgeBaseEnableStatus(UpdateKnowledgeBaseEnableStatusRequest request)
    {
        // Validate whether it already exists.
        KnowledgeBaseMetadata knowledgeBaseMetadata = knowledgeBaseMapper.selectById(request.getKnowledgeBaseId());
        if (knowledgeBaseMetadata == null)
        {
            return ServiceResponse.buildErrorResponse(
                    KnowledgeManagerBusinessError.KNOWLEDGE_BASE_NOT_EXISTS.getCode(),
                    KnowledgeManagerBusinessError.KNOWLEDGE_BASE_NOT_EXISTS.getMessage()
            );
        }

        // Validate request parameters.
        if (knowledgeBaseMetadata.getChunkType() == ChunkType.DOCUMENT_IMAGE)
        {
            return ServiceResponse.buildErrorResponse(
                    KnowledgeManagerBusinessError.ERROR_BAD_REQUEST.getCode(),
                    KnowledgeManagerBusinessError.ERROR_BAD_REQUEST.getMessage()
            );
        }

        // Update knowledge base metadata's 'enabled' field.
        long userId = UserContext.getCurrentUserId();
        knowledgeBaseMetadata.setEnabled(request.isEnabled());
        knowledgeBaseMetadata.setModifierId(userId);
        knowledgeBaseMapper.updateById(knowledgeBaseMetadata);

        // Update document image knowledge base metadata's 'enabled' field.
        if (knowledgeBaseMetadata.getChunkType() == ChunkType.DOCUMENT)
        {
            KnowledgeBaseMetadata documentImageKnowledgeBaseMetadata = knowledgeBaseMapper.selectOne(Wrappers.lambdaQuery(KnowledgeBaseMetadata.class)
                                                                                                             .eq(KnowledgeBaseMetadata::getBizId, knowledgeBaseMetadata.getBizId())
                                                                                                             .eq(KnowledgeBaseMetadata::getName, knowledgeBaseMetadata.getName())
                                                                                                             .eq(KnowledgeBaseMetadata::getChunkType, ChunkType.DOCUMENT_IMAGE));
            documentImageKnowledgeBaseMetadata.setEnabled(request.isEnabled());
            documentImageKnowledgeBaseMetadata.setModifierId(userId);
            knowledgeBaseMapper.updateById(documentImageKnowledgeBaseMetadata);
        }

        return ServiceResponse.buildSuccessResponse(true);
    }

    @Override
    public ServiceResponse<KnowledgeBaseDetail> getKnowledgeBaseDetail(long knowledgeBaseId)
    {
        // Validate whether it already exists.
        KnowledgeBaseMetadata knowledgeBaseMetadata = knowledgeBaseMapper.selectById(knowledgeBaseId);
        if (knowledgeBaseMetadata == null)
        {
            return ServiceResponse.buildErrorResponse(
                    KnowledgeManagerBusinessError.KNOWLEDGE_BASE_NOT_EXISTS.getCode(),
                    KnowledgeManagerBusinessError.KNOWLEDGE_BASE_NOT_EXISTS.getMessage()
            );
        }

        // Construct response data.
        KnowledgeBaseDetail response = new KnowledgeBaseDetail();
        response.setKnowledgeBaseId(knowledgeBaseMetadata.getId());
        response.setBizId(knowledgeBaseMetadata.getBizId());
        response.setName(knowledgeBaseMetadata.getName());
        response.setChunkType(knowledgeBaseMetadata.getChunkType());
        response.setDescription(knowledgeBaseMetadata.getDescription());
        response.setEmbeddingModel(knowledgeBaseMetadata.getEmbeddingModel());
        response.setEmbeddingDimensionCount(knowledgeBaseMetadata.getEmbeddingDimensionCount());
        response.setEnabled(knowledgeBaseMetadata.isEnabled());
        if (knowledgeBaseMetadata.getChunkType() == ChunkType.DOCUMENT)
        {
            response.setMinChunkSize(knowledgeBaseMetadata.getMinChunkSize());
            response.setChunkOverlap(knowledgeBaseMetadata.getChunkOverlap());
        }
        response.setCreationTime(knowledgeBaseMetadata.getCreationTime());
        response.setModificationTime(knowledgeBaseMetadata.getModificationTime());
        response.setChunkCount(selectChunkCount(buildChunkTableName(knowledgeBaseMetadata.getChunkType(), knowledgeBaseMetadata.getName(), knowledgeBaseMetadata.getBizId())));
        return ServiceResponse.buildSuccessResponse(response);
    }

    @Override
    public ServiceResponse<PageResponse<KnowledgeBaseAbstract>> pageKnowledgeBases(PageKnowledgeBasesRequest request)
    {
        // Construct query conditions accordingly.
        LambdaQueryWrapper<KnowledgeBaseMetadata> queryWrapper = null;
        if (request.getKeyword() != null)
        {
            queryWrapper = Wrappers.lambdaQuery(KnowledgeBaseMetadata.class)
                                   .eq(request.getBizId() != null, KnowledgeBaseMetadata::getBizId, request.getBizId())
                                   .like(request.getKeyword() != null && !request.getKeyword()
                                                                                 .isBlank(), KnowledgeBaseMetadata::getName, request.getKeyword()
                                                                                                                                    .strip()
                                                                                                                                    .toLowerCase(Locale.ROOT))
                                   .eq(request.getChunkType() != null, KnowledgeBaseMetadata::getChunkType, request.getChunkType())
                                   .eq(request.getEnabled() != null, KnowledgeBaseMetadata::isEnabled, request.getEnabled())
                                   .orderByDesc(KnowledgeBaseMetadata::getModificationTime);
        }
        Page<KnowledgeBaseMetadata> page = new Page<>(request.getPageNumber(), request.getPageSize());

        // Select by page.
        Page<KnowledgeBaseMetadata> result = knowledgeBaseMapper.selectPage(page, queryWrapper);

        // Convert 'KnowledgeBaseMetadata' to 'KnowledgeBaseAbstract'.
        List<KnowledgeBaseAbstract> records = result.getRecords()
                                                    .stream()
                                                    .map(metadata ->
                                                    {
                                                        KnowledgeBaseAbstract anAbstract = new KnowledgeBaseAbstract();
                                                        anAbstract.setKnowledgeBaseId(metadata.getId());
                                                        anAbstract.setBizId(metadata.getBizId());
                                                        anAbstract.setName(metadata.getName());
                                                        anAbstract.setChunkType(metadata.getChunkType());
                                                        anAbstract.setEmbeddingModel(metadata.getEmbeddingModel());
                                                        anAbstract.setEnabled(metadata.isEnabled());
                                                        anAbstract.setModificationTime(metadata.getModificationTime());
                                                        return anAbstract;
                                                    })
                                                    .toList();

        // Build page response.
        PageResponse<KnowledgeBaseAbstract> response = new PageResponse<>();
        response.setTotal(result.getTotal());
        response.setPageNumber((int) result.getCurrent());
        response.setPageSize((int) result.getSize());
        response.setRecords(records);

        return ServiceResponse.buildSuccessResponse(response);
    }

    private String buildChunkTableName(ChunkType chunkType, String name, long bizId)
    {
        return chunkType.name()
                        .toLowerCase(Locale.ROOT) + "_" + name + "_" + bizId;
    }

    private void createChunkTable(ChunkType chunkType, String chunkTableName, int embeddingDimensionCount)
    {
        String sql = "";

        switch (chunkType)
        {
            case DOCUMENT -> sql =
                    """
                            create table %s
                            (
                                id bigserial primary key,
                                creator_id bigint,
                                creation_time timestamptz default current_timestamp,
                                modifier_id bigint,
                                modification_time timestamptz default current_timestamp,
                                knowledge_base_id bigint,
                                chunk_abstract text,
                                token_count int,
                                embedding vector(%d),
                                document_id bigint,
                                chunk_number int,
                                section_number varchar(1024),
                                chunk_content text,
                                previous_chunk_abstract text,
                                next_chunk_abstract text,
                                referenced_image_chunk_ids jsonb
                            );
                            """.formatted(chunkTableName, embeddingDimensionCount);
            case DOCUMENT_IMAGE -> sql =
                    """
                            create table %s
                            (
                                id bigserial primary key,
                                creator_id bigint,
                                creation_time timestamptz default current_timestamp,
                                modifier_id bigint,
                                modification_time timestamptz default current_timestamp,
                                knowledge_base_id bigint,
                                chunk_abstract text,
                                token_count int,
                                embedding vector(%d),
                                document_id bigint,
                                width int,
                                height int,
                                section_number varchar(1024),
                                description text,
                                name_in_oss varchar(1024)
                            );
                            """.formatted(chunkTableName, embeddingDimensionCount);
            case IMAGE -> sql =
                    """
                            create table %s
                            (
                                id bigserial primary key,
                                creator_id bigint,
                                creation_time timestamptz default current_timestamp,
                                modifier_id bigint,
                                modification_time timestamptz default current_timestamp,
                                knowledge_base_id bigint,
                                chunk_abstract text,
                                token_count int,
                                embedding vector(%d),
                                width int,
                                height int,
                                image_url varchar(1024)
                            );
                            """.formatted(chunkTableName, embeddingDimensionCount);
            case VIDEO -> sql =
                    """
                            create table %s
                            (
                                id bigserial primary key,
                                creator_id bigint,
                                creation_time timestamptz default current_timestamp,
                                modifier_id bigint,
                                modification_time timestamptz default current_timestamp,
                                knowledge_base_id bigint,
                                chunk_abstract text,
                                token_count int,
                                embedding vector(%d),
                                video_url varchar(1024),
                                width int,
                                height int,
                                title varchar(1024),
                                introduction text,
                                tags jsonb
                            );
                            """.formatted(chunkTableName, embeddingDimensionCount);
        }

        jdbcTemplate.execute(sql);
    }

    private void renameChunkTable(String oldChunkTableName, String newChunkTableName)
    {
        String sql = "alter table if exists " + oldChunkTableName + " rename to " + newChunkTableName;

        jdbcTemplate.execute(sql);
    }

    private void dropChunkTable(String chunkTableName)
    {
        String sql = "drop table if exists " + chunkTableName;

        jdbcTemplate.execute(sql);
    }

    private long selectChunkCount(String chunkTableName)
    {
        String sql = "select count(*) from " + chunkTableName;

        Long count = jdbcTemplate.queryForObject(sql, Long.class);
        return count == null ? 0 : count;
    }
}
