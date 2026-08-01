package ifl.agentbreaker.knowledgemanager.services;

import com.baomidou.mybatisplus.extension.service.IService;
import ifl.agentbreaker.knowledgemanager.domain.dtos.requests.ImportVideoRequest;
import ifl.agentbreaker.knowledgemanager.domain.dtos.requests.PageVideoChunksRequest;
import ifl.agentbreaker.knowledgemanager.domain.dtos.responses.PageResponse;
import ifl.agentbreaker.knowledgemanager.domain.dtos.responses.VideoChunkDetailResponse;
import ifl.agentbreaker.knowledgemanager.domain.dtos.responses.VideoChunkResponse;
import ifl.agentbreaker.knowledgemanager.domain.entities.pg.VideoChunkBase;
import jakarta.validation.Valid;
import stark.dataworks.boot.web.ServiceResponse;

public interface VideoChunkService extends IService<VideoChunkBase>
{
    ServiceResponse<Boolean> importVideo(@Valid ImportVideoRequest request);

    ServiceResponse<Boolean> deleteVideoChunk(long chunkId);

    ServiceResponse<PageResponse<VideoChunkResponse>> pageVideoChunks(@Valid PageVideoChunksRequest request);

    ServiceResponse<VideoChunkDetailResponse> getVideoChunkDetail(long chunkId);
}
