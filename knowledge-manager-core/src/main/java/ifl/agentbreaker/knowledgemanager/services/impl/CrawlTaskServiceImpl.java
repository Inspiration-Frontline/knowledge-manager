package ifl.agentbreaker.knowledgemanager.services.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import ifl.agentbreaker.knowledgemanager.domain.entities.pg.CrawlTask;
import ifl.agentbreaker.knowledgemanager.mappers.CrawlTaskMapper;
import ifl.agentbreaker.knowledgemanager.services.CrawlTaskService;
import org.springframework.stereotype.Service;

@Service
public class CrawlTaskServiceImpl extends ServiceImpl<CrawlTaskMapper, CrawlTask> implements CrawlTaskService
{
}
