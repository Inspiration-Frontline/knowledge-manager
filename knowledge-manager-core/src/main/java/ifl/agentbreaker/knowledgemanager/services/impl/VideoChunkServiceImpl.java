package ifl.agentbreaker.knowledgemanager.services.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import ifl.agentbreaker.knowledgemanager.domain.dtos.requests.ImportVideoRequest;
import ifl.agentbreaker.knowledgemanager.domain.dtos.requests.PageVideoChunksRequest;
import ifl.agentbreaker.knowledgemanager.domain.dtos.responses.PageResponse;
import ifl.agentbreaker.knowledgemanager.domain.dtos.responses.VideoChunkDetailResponse;
import ifl.agentbreaker.knowledgemanager.domain.dtos.responses.VideoChunkResponse;
import ifl.agentbreaker.knowledgemanager.domain.entities.pg.VideoChunkBase;
import ifl.agentbreaker.knowledgemanager.mappers.VideoChunkMapper;
import ifl.agentbreaker.knowledgemanager.services.VideoChunkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import stark.dataworks.boot.web.ServiceResponse;

@Service
public class VideoChunkServiceImpl extends ServiceImpl<VideoChunkMapper, VideoChunkBase> implements VideoChunkService
{
    @Autowired
    private VideoChunkMapper videoChunkMapper;

    @Override
    public ServiceResponse<Boolean> importVideo(ImportVideoRequest request)
    {
        return null;
    }

    @Override
    public ServiceResponse<Boolean> deleteVideoChunk(long chunkId)
    {
        return null;
    }

    @Override
    public ServiceResponse<PageResponse<VideoChunkResponse>> pageVideoChunks(PageVideoChunksRequest request)
    {
        return null;
    }

    @Override
    public ServiceResponse<VideoChunkDetailResponse> getVideoChunkDetail(long chunkId)
    {
        return null;
    }
}
