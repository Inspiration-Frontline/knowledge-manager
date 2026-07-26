package ifl.agentbreaker.knowledgemanager.services;

import com.baomidou.mybatisplus.extension.service.IService;
import ifl.agentbreaker.knowledgemanager.domain.dtos.requests.PageChunksRequest;
import ifl.agentbreaker.knowledgemanager.domain.dtos.requests.SearchChunksRequest;
import ifl.agentbreaker.knowledgemanager.domain.dtos.responses.ChunkAbstract;
import ifl.agentbreaker.knowledgemanager.domain.dtos.responses.ChunkDetailResponse;
import ifl.agentbreaker.knowledgemanager.domain.dtos.responses.ChunkSearchResponse;
import ifl.agentbreaker.knowledgemanager.domain.dtos.responses.PageResponse;
import ifl.agentbreaker.knowledgemanager.domain.entities.pg.KnowledgeBaseChunkBase;
import jakarta.validation.Valid;
import stark.dataworks.boot.web.ServiceResponse;

import java.util.List;

public interface ChunkService extends IService<KnowledgeBaseChunkBase>
{
    ServiceResponse<ChunkDetailResponse> getChunkDetail(long chunkId);

    ServiceResponse<PageResponse<ChunkAbstract>> getChunkAbstracts(@Valid PageChunksRequest request);

    ServiceResponse<List<ChunkSearchResponse>> queryChunks(@Valid SearchChunksRequest request);
}
