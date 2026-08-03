package ifl.agentbreaker.knowledgemanager.services;

import com.baomidou.mybatisplus.extension.service.IService;
import ifl.agentbreaker.knowledgemanager.domain.dtos.requests.*;
import ifl.agentbreaker.knowledgemanager.domain.dtos.responses.PageResponse;
import ifl.agentbreaker.knowledgemanager.domain.dtos.responses.SyncTaskDetail;
import ifl.agentbreaker.knowledgemanager.domain.dtos.responses.SyncTaskExecutionDetail;
import ifl.agentbreaker.knowledgemanager.domain.dtos.responses.SyncTaskAbstract;
import ifl.agentbreaker.knowledgemanager.domain.entities.pg.SyncTask;
import jakarta.validation.Valid;
import stark.dataworks.boot.web.ServiceResponse;

public interface SyncTaskService extends IService<SyncTask>
{
    ServiceResponse<Boolean> createSyncTask(@Valid CreateSyncTaskRequest request);

    ServiceResponse<Boolean> updateSyncTask(@Valid UpdateSyncTaskRequest request);

    ServiceResponse<Boolean> deleteSyncTask(long syncTaskId);

    ServiceResponse<Boolean> updateSyncTaskEnableStatus(UpdateSyncTaskEnableStatusRequest request);

    ServiceResponse<SyncTaskDetail> getSyncTaskDetail(long syncTaskId);

    ServiceResponse<PageResponse<SyncTaskExecutionDetail>> pageSyncTaskExecutions(@Valid PageSyncTaskExecutionsRequest request);

    ServiceResponse<PageResponse<SyncTaskAbstract>> pageSyncTasks(@Valid PageSyncTasksRequest request);

    ServiceResponse<Boolean> executeSyncTask(long syncTaskId);
}
