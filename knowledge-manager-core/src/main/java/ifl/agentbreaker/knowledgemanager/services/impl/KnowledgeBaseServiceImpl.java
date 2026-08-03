package ifl.agentbreaker.knowledgemanager.services.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import ifl.agentbreaker.knowledgemanager.domain.constants.ChunkType;
import ifl.agentbreaker.knowledgemanager.domain.dtos.requests.CreateKnowledgeBaseRequest;
import ifl.agentbreaker.knowledgemanager.domain.dtos.requests.PageKnowledgeBasesRequest;
import ifl.agentbreaker.knowledgemanager.domain.dtos.requests.UpdateKnowledgeBaseEnableStatusRequest;
import ifl.agentbreaker.knowledgemanager.domain.dtos.requests.UpdateKnowledgeBaseRequest;
import ifl.agentbreaker.knowledgemanager.domain.dtos.responses.KnowledgeBaseDetail;
import ifl.agentbreaker.knowledgemanager.domain.dtos.responses.KnowledgeBaseAbstract;
import ifl.agentbreaker.knowledgemanager.domain.dtos.responses.PageResponse;
import ifl.agentbreaker.knowledgemanager.domain.entities.pg.KnowledgeBaseMetadata;
import ifl.agentbreaker.knowledgemanager.exception.KnowledgeManagerBusinessError;
import ifl.agentbreaker.knowledgemanager.mappers.KnowledgeBaseMapper;
import ifl.agentbreaker.knowledgemanager.services.KnowledgeBaseService;
import ifl.agentbreaker.knowledgemanager.utils.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import stark.dataworks.boot.web.ServiceResponse;

import java.util.Locale;

@Service
public class KnowledgeBaseServiceImpl extends ServiceImpl<KnowledgeBaseMapper, KnowledgeBaseMetadata> implements KnowledgeBaseService
{
    @Autowired
    private KnowledgeBaseMapper knowledgeBaseMapper;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    @Transactional
    public ServiceResponse<Boolean> createKnowledgeBase(CreateKnowledgeBaseRequest request)
    {
        // Validate request parameters.
        if (request.getBizId() <= 0)
        {
            return ServiceResponse.buildErrorResponse(
                    KnowledgeManagerBusinessError.ERROR_BAD_REQUEST.getCode(),
                    KnowledgeManagerBusinessError.ERROR_BAD_REQUEST.getMessage());
        }
        if (request.getEmbeddingDimensionCount() <= 0)
        {
            return ServiceResponse.buildErrorResponse(
                    KnowledgeManagerBusinessError.ERROR_BAD_REQUEST.getCode(),
                    KnowledgeManagerBusinessError.ERROR_BAD_REQUEST.getMessage());
        }
        if (request.getChunkType() == ChunkType.DOCUMENT_IMAGE)
        {
            return ServiceResponse.buildErrorResponse(
                    KnowledgeManagerBusinessError.ERROR_BAD_REQUEST.getCode(),
                    KnowledgeManagerBusinessError.ERROR_BAD_REQUEST.getMessage()
            );
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
                                             .toLowerCase(Locale.ROOT));
        knowledgeBaseMetadata.setChunkType(request.getChunkType());
        knowledgeBaseMetadata.setDescription(request.getDescription());
        knowledgeBaseMetadata.setEmbeddingModel(request.getEmbeddingModel());
        knowledgeBaseMetadata.setEmbeddingDimensionCount(request.getEmbeddingDimensionCount());
        knowledgeBaseMetadata.setEnabled(true);
        knowledgeBaseMetadata.setMinChunkSize(request.getMinChunkSize());
        knowledgeBaseMetadata.setChunkOverlap(request.getChunkOverlap());
        knowledgeBaseMapper.insert(knowledgeBaseMetadata);

        // Dynamically create chunk table accordingly.
        createChunkTable(request.getChunkType(),
                request.getName()
                       .toLowerCase(Locale.ROOT),
                request.getBizId(),
                request.getEmbeddingDimensionCount());

        // Insert document image knowledge base metadata into database.
        if (request.getChunkType() == ChunkType.DOCUMENT)
        {
            KnowledgeBaseMetadata documentImageKnowledgeBaseMetadata = new KnowledgeBaseMetadata();
            documentImageKnowledgeBaseMetadata.setCreatorId(userId);
            documentImageKnowledgeBaseMetadata.setModifierId(userId);
            documentImageKnowledgeBaseMetadata.setBizId(request.getBizId());
            documentImageKnowledgeBaseMetadata.setName(request.getName()
                                                              .toLowerCase());
            documentImageKnowledgeBaseMetadata.setChunkType(ChunkType.DOCUMENT_IMAGE);
            documentImageKnowledgeBaseMetadata.setDescription(request.getDescription());
            documentImageKnowledgeBaseMetadata.setEmbeddingModel(request.getDocumentImageEmbeddingModel());
            documentImageKnowledgeBaseMetadata.setEmbeddingDimensionCount(request.getDocumentImageEmbeddingDimensionCount());
            documentImageKnowledgeBaseMetadata.setEnabled(true);
            knowledgeBaseMapper.insert(documentImageKnowledgeBaseMetadata);

            // Dynamically create document image chunk table if chunk type is document.
            createChunkTable(ChunkType.DOCUMENT_IMAGE,
                    request.getName()
                           .toLowerCase(Locale.ROOT),
                    request.getBizId(),
                    request.getDocumentImageEmbeddingDimensionCount());
        }

        return ServiceResponse.buildSuccessResponse(true);
    }

    private void createChunkTable(ChunkType chunkType, String name, long bizId, int embeddingDimensionCount)
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
                            """.formatted(chunkType.toString()
                                                   .toLowerCase(Locale.ROOT) + "_" + name + "_" + bizId, embeddingDimensionCount);
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
                            """.formatted(chunkType.toString()
                                                   .toLowerCase(Locale.ROOT) + "_" + name + "_" + bizId, embeddingDimensionCount);
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
                            """.formatted(chunkType.toString()
                                                   .toLowerCase(Locale.ROOT) + "_" + name + "_" + bizId, embeddingDimensionCount);
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
                            """.formatted(chunkType.toString()
                                                   .toLowerCase(Locale.ROOT) + "_" + name + "_" + bizId, embeddingDimensionCount);
        }

        jdbcTemplate.execute(sql);
    }

    // 文档图片只允许单独更改model和dimensions，其他字段包括bizId和enabled都只能跟文档共进退
    @Override
    public ServiceResponse<Boolean> updateKnowledgeBase(UpdateKnowledgeBaseRequest request)
    {
        return null;
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
        dropChunkTable(knowledgeBaseMetadata.getChunkType(), knowledgeBaseMetadata.getName(), knowledgeBaseMetadata.getBizId());

        // Delete document image knowledge base metadata.
        if (knowledgeBaseMetadata.getChunkType() == ChunkType.DOCUMENT)
        {
            KnowledgeBaseMetadata documentImageKnowledgeBaseMetadata = knowledgeBaseMapper.selectOne(Wrappers.lambdaQuery(KnowledgeBaseMetadata.class)
                                                                                                             .eq(KnowledgeBaseMetadata::getBizId, knowledgeBaseMetadata.getBizId())
                                                                                                             .eq(KnowledgeBaseMetadata::getName, knowledgeBaseMetadata.getName())
                                                                                                             .eq(KnowledgeBaseMetadata::getChunkType, ChunkType.DOCUMENT_IMAGE));
            knowledgeBaseMapper.deleteById(documentImageKnowledgeBaseMetadata);

            // Drop document image chunk table if chunk type is document.
            dropChunkTable(ChunkType.DOCUMENT_IMAGE, documentImageKnowledgeBaseMetadata.getName(), documentImageKnowledgeBaseMetadata.getBizId());
        }


        return ServiceResponse.buildSuccessResponse(true);
    }

    private void dropChunkTable(ChunkType chunkType, String name, long bizId)
    {
        String sql = "drop table if exists " + chunkType.toString().toLowerCase(Locale.ROOT) + "_" + name + "_" + bizId;

        jdbcTemplate.execute(sql);
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
        response.setChunkCount(selectChunkCount(knowledgeBaseMetadata.getChunkType(), knowledgeBaseMetadata.getName(), knowledgeBaseMetadata.getBizId()));
        return ServiceResponse.buildSuccessResponse(response);
    }

    private long selectChunkCount(ChunkType chunkType, String name, long bizId)
    {
        String sql = "select count(*) from " + chunkType.toString().toLowerCase(Locale.ROOT) + "_" + name + "_" + bizId;

        Long count = jdbcTemplate.queryForObject(sql, Long.class);
        return count == null ? 0 : count;
    }

    @Override
    public ServiceResponse<PageResponse<KnowledgeBaseAbstract>> pageKnowledgeBases(PageKnowledgeBasesRequest request)
    {

        return null;
    }
}
