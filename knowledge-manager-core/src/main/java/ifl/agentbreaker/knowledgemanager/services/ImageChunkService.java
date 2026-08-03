package ifl.agentbreaker.knowledgemanager.services;

import com.baomidou.mybatisplus.extension.service.IService;
import ifl.agentbreaker.knowledgemanager.domain.dtos.requests.ImportImageRequest;
import ifl.agentbreaker.knowledgemanager.domain.dtos.requests.PageImageChunksRequest;
import ifl.agentbreaker.knowledgemanager.domain.dtos.responses.ImageChunkDetail;
import ifl.agentbreaker.knowledgemanager.domain.dtos.responses.ImageChunkAbstract;
import ifl.agentbreaker.knowledgemanager.domain.dtos.responses.PageResponse;
import ifl.agentbreaker.knowledgemanager.domain.entities.pg.ImageChunkBase;
import jakarta.validation.Valid;
import stark.dataworks.boot.web.ServiceResponse;

public interface ImageChunkService extends IService<ImageChunkBase>
{
    ServiceResponse<Boolean> importImage(@Valid ImportImageRequest request);

    ServiceResponse<Boolean> deleteImageChunk(long chunkId);

    ServiceResponse<PageResponse<ImageChunkAbstract>> pageImageChunks(@Valid PageImageChunksRequest request);

    ServiceResponse<ImageChunkDetail> getImageChunkDetail(long chunkId);
}
