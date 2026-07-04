package ifl.agentbreaker.knowledgemanager.services.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import ifl.agentbreaker.knowledgemanager.domain.entities.pg.KnowledgeBase;
import ifl.agentbreaker.knowledgemanager.mappers.KnowledgeBaseMapper;
import ifl.agentbreaker.knowledgemanager.services.KnowledgeBaseService;
import org.springframework.stereotype.Service;

@Service
public class KnowledgeBaseServiceImpl extends ServiceImpl<KnowledgeBaseMapper, KnowledgeBase> implements KnowledgeBaseService
{
}
