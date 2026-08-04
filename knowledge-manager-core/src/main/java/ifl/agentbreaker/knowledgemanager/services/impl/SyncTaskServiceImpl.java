package ifl.agentbreaker.knowledgemanager.services.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import ifl.agentbreaker.knowledgemanager.domain.dtos.requests.*;
import ifl.agentbreaker.knowledgemanager.domain.dtos.responses.PageResponse;
import ifl.agentbreaker.knowledgemanager.domain.dtos.responses.SyncTaskDetail;
import ifl.agentbreaker.knowledgemanager.domain.dtos.responses.SyncTaskExecutionDetail;
import ifl.agentbreaker.knowledgemanager.domain.dtos.responses.SyncTaskAbstract;
import ifl.agentbreaker.knowledgemanager.domain.entities.pg.SyncTask;
import ifl.agentbreaker.knowledgemanager.mappers.SyncTaskExecutionMapper;
import ifl.agentbreaker.knowledgemanager.mappers.SyncTaskMapper;
import ifl.agentbreaker.knowledgemanager.services.SyncTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import stark.dataworks.boot.web.ServiceResponse;

@Service
public class SyncTaskServiceImpl extends ServiceImpl<SyncTaskMapper, SyncTask> implements SyncTaskService
{
    @Autowired
    private SyncTaskMapper syncTaskMapper;

    @Autowired
    private SyncTaskExecutionMapper syncTaskExecutionMapper;

    @Override
    public ServiceResponse<Boolean> createSyncTask(CreateSyncTaskRequest request)
    {
        return null;
    }

    @Override
    public ServiceResponse<Boolean> updateSyncTask(UpdateSyncTaskRequest request)
    {
        return null;
    }

    @Override
    public ServiceResponse<Boolean> deleteSyncTask(long syncTaskId)
    {
        return null;
    }

    @Override
    public ServiceResponse<Boolean> updateSyncTaskEnableStatus(UpdateSyncTaskEnableStatusRequest request)
    {
        return null;
    }

    @Override
    public ServiceResponse<SyncTaskDetail> getSyncTaskDetail(long syncTaskId)
    {
        return null;
    }

    @Override
    public ServiceResponse<PageResponse<SyncTaskExecutionDetail>> pageSyncTaskExecutions(PageSyncTaskExecutionsRequest request)
    {
        return null;
    }

    @Override
    public ServiceResponse<PageResponse<SyncTaskAbstract>> pageSyncTasks(PageSyncTasksRequest request)
    {
        return null;
    }

    @Override
    public ServiceResponse<Boolean> executeSyncTask(long syncTaskId)
    {
        return null;
    }
}
