package ifl.agentbreaker.knowledgemanager.services.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import ifl.agentbreaker.knowledgemanager.domain.dtos.requests.ImportImageRequest;
import ifl.agentbreaker.knowledgemanager.domain.dtos.requests.PageImageChunksRequest;
import ifl.agentbreaker.knowledgemanager.domain.dtos.responses.ImageChunkDetailResponse;
import ifl.agentbreaker.knowledgemanager.domain.dtos.responses.ImageChunkResponse;
import ifl.agentbreaker.knowledgemanager.domain.dtos.responses.PageResponse;
import ifl.agentbreaker.knowledgemanager.domain.entities.pg.ImageChunkBase;
import ifl.agentbreaker.knowledgemanager.mappers.ImageChunkMapper;
import ifl.agentbreaker.knowledgemanager.services.ImageChunkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import stark.dataworks.boot.web.ServiceResponse;

@Service
public class ImageChunkServiceImpl extends ServiceImpl<ImageChunkMapper, ImageChunkBase> implements ImageChunkService
{
    @Autowired
    private ImageChunkMapper imageChunkMapper;

    @Override
    public ServiceResponse<Boolean> importImage(ImportImageRequest request)
    {
        return null;
    }

    @Override
    public ServiceResponse<Boolean> deleteImageChunk(long chunkId)
    {
        return null;
    }

    @Override
    public ServiceResponse<PageResponse<ImageChunkResponse>> pageImageChunks(PageImageChunksRequest request)
    {
        return null;
    }

    @Override
    public ServiceResponse<ImageChunkDetailResponse> getImageChunkDetail(long chunkId)
    {
        return null;
    }
}
