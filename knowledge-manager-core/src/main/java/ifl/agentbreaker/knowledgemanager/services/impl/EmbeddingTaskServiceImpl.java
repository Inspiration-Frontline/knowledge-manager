package ifl.agentbreaker.knowledgemanager.services.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import ifl.agentbreaker.knowledgemanager.domain.entities.pg.EmbeddingTask;
import ifl.agentbreaker.knowledgemanager.mappers.EmbeddingTaskMapper;
import ifl.agentbreaker.knowledgemanager.services.EmbeddingTaskService;
import org.springframework.stereotype.Service;

@Service
public class EmbeddingTaskServiceImpl extends ServiceImpl<EmbeddingTaskMapper, EmbeddingTask> implements EmbeddingTaskService
{
}
