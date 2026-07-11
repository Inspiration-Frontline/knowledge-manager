package ifl.agentbreaker.knowledgemanager.services;

import com.baomidou.mybatisplus.extension.service.IService;
import ifl.agentbreaker.knowledgemanager.domain.dtos.responses.ChunkDetailResponse;
import ifl.agentbreaker.knowledgemanager.domain.entities.pg.KnowledgeBaseChunkBase;
import stark.dataworks.boot.web.ServiceResponse;

public interface ChunkService extends IService<KnowledgeBaseChunkBase>
{
    ServiceResponse<ChunkDetailResponse> getChunkDetail(long chunkId);
}
