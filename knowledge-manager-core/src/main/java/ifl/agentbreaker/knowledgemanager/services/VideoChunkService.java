package ifl.agentbreaker.knowledgemanager.services;

import com.baomidou.mybatisplus.extension.service.IService;
import ifl.agentbreaker.knowledgemanager.domain.dtos.requests.ImportVideoRequest;
import ifl.agentbreaker.knowledgemanager.domain.dtos.requests.PageVideoChunksRequest;
import ifl.agentbreaker.knowledgemanager.domain.dtos.responses.PageResponse;
import ifl.agentbreaker.knowledgemanager.domain.dtos.responses.VideoChunkDetail;
import ifl.agentbreaker.knowledgemanager.domain.dtos.responses.VideoChunkAbstract;
import ifl.agentbreaker.knowledgemanager.domain.entities.pg.VideoChunkBase;
import jakarta.validation.Valid;
import stark.dataworks.boot.web.ServiceResponse;

public interface VideoChunkService extends IService<VideoChunkBase>
{
    ServiceResponse<Boolean> importVideo(@Valid ImportVideoRequest request);

    ServiceResponse<Boolean> deleteVideoChunk(long chunkId);

    ServiceResponse<PageResponse<VideoChunkAbstract>> pageVideoChunks(@Valid PageVideoChunksRequest request);

    ServiceResponse<VideoChunkDetail> getVideoChunkDetail(long chunkId);
}
