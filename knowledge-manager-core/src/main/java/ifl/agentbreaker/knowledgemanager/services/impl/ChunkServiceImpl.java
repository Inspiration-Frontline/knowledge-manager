package ifl.agentbreaker.knowledgemanager.services.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import ifl.agentbreaker.knowledgemanager.domain.entities.pg.Chunk;
import ifl.agentbreaker.knowledgemanager.mappers.ChunkMapper;
import ifl.agentbreaker.knowledgemanager.services.ChunkService;
import org.springframework.stereotype.Service;

@Service
public class ChunkServiceImpl extends ServiceImpl<ChunkMapper, Chunk> implements ChunkService
{
}
