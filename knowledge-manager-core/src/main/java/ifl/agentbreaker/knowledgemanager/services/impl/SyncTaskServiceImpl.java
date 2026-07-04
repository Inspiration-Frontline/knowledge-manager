package ifl.agentbreaker.knowledgemanager.services.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import ifl.agentbreaker.knowledgemanager.domain.entities.pg.SyncTask;
import ifl.agentbreaker.knowledgemanager.mappers.SyncTaskMapper;
import ifl.agentbreaker.knowledgemanager.services.SyncTaskService;
import org.springframework.stereotype.Service;

@Service
public class SyncTaskServiceImpl extends ServiceImpl<SyncTaskMapper, SyncTask> implements SyncTaskService
{
}
