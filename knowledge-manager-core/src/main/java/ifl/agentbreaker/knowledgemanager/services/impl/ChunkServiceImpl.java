package ifl.agentbreaker.knowledgemanager.services.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import ifl.agentbreaker.knowledgemanager.domain.dtos.responses.ChunkDetailResponse;
import ifl.agentbreaker.knowledgemanager.domain.entities.pg.KnowledgeBaseChunkBase;
import ifl.agentbreaker.knowledgemanager.mappers.ChunkMapper;
import ifl.agentbreaker.knowledgemanager.services.ChunkService;
import org.springframework.stereotype.Service;
import stark.dataworks.boot.web.ServiceResponse;

@Service
public class ChunkServiceImpl extends ServiceImpl<ChunkMapper, KnowledgeBaseChunkBase> implements ChunkService
{
    public ServiceResponse<ChunkDetailResponse> getChunkDetail(long chunkId)
    {
        return null;
    }
}
