package ifl.agentbreaker.knowledgemanager.services.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import ifl.agentbreaker.knowledgemanager.domain.entities.pg.Document;
import ifl.agentbreaker.knowledgemanager.mappers.DocumentMapper;
import ifl.agentbreaker.knowledgemanager.services.DocumentService;
import org.springframework.stereotype.Service;

@Service
public class DocumentServiceImpl extends ServiceImpl<DocumentMapper, Document> implements DocumentService
{
}
