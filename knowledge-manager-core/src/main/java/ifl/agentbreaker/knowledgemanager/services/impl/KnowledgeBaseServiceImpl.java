package ifl.agentbreaker.knowledgemanager.services.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import ifl.agentbreaker.knowledgemanager.domain.constants.ChunkType;
import ifl.agentbreaker.knowledgemanager.domain.dtos.requests.CreateKnowledgeBaseRequest;
import ifl.agentbreaker.knowledgemanager.domain.dtos.requests.PageKnowledgeBasesRequest;
import ifl.agentbreaker.knowledgemanager.domain.dtos.requests.UpdateKnowledgeBaseEnableStatusRequest;
import ifl.agentbreaker.knowledgemanager.domain.dtos.requests.UpdateKnowledgeBaseRequest;
import ifl.agentbreaker.knowledgemanager.domain.dtos.responses.KnowledgeBaseDetailResponse;
import ifl.agentbreaker.knowledgemanager.domain.dtos.responses.KnowledgeBaseResponse;
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
        if (!request.getChunkType().isUserCreatable())
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

        // Insert document image knowledge base metadata into database.
        if (request.getChunkType() == ChunkType.DOCUMENT)
        {
            KnowledgeBaseMetadata imageKnowledgeBaseMetaData = new KnowledgeBaseMetadata();
            imageKnowledgeBaseMetaData.setCreatorId(userId);
            imageKnowledgeBaseMetaData.setModifierId(userId);
            imageKnowledgeBaseMetaData.setBizId(request.getBizId());
            imageKnowledgeBaseMetaData.setName(request.getName()
                                                      .toLowerCase());
            imageKnowledgeBaseMetaData.setChunkType(ChunkType.DOCUMENT_IMAGE);
            imageKnowledgeBaseMetaData.setDescription(request.getDescription());
            imageKnowledgeBaseMetaData.setEmbeddingModel(request.getDocumentImageEmbeddingModel());
            imageKnowledgeBaseMetaData.setEmbeddingDimensionCount(request.getDocumentImageEmbeddingDimensionCount());
            imageKnowledgeBaseMetaData.setEnabled(true);
            knowledgeBaseMapper.insert(imageKnowledgeBaseMetaData);

            createChunkTable(ChunkType.DOCUMENT_IMAGE,
                    request.getName()
                           .toLowerCase(Locale.ROOT),
                    request.getBizId(),
                    request.getDocumentImageEmbeddingDimensionCount());
        }

        // Dynamically create two chunk tables according to 'chunkType'.
        createChunkTable(request.getChunkType(),
                request.getName()
                       .toLowerCase(Locale.ROOT),
                request.getBizId(),
                request.getEmbeddingDimensionCount());

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

    @Override
    public ServiceResponse<Boolean> updateKnowledgeBase(UpdateKnowledgeBaseRequest request)
    {
        return null;
    }

    @Override
    public ServiceResponse<Boolean> deleteKnowledgeBase(long knowledgeBaseId)
    {
        return null;
    }

    @Override
    public ServiceResponse<Boolean> updateKnowledgeBaseEnableStatus(UpdateKnowledgeBaseEnableStatusRequest request)
    {
        return null;
    }

    @Override
    public ServiceResponse<KnowledgeBaseDetailResponse> getKnowledgeBaseDetail(long knowledgeBaseId)
    {
        return null;
    }

    @Override
    public ServiceResponse<PageResponse<KnowledgeBaseResponse>> pageKnowledgeBases(PageKnowledgeBasesRequest request)
    {
        return null;
    }
}
