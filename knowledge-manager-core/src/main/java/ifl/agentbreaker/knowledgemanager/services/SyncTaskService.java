package ifl.agentbreaker.knowledgemanager.services;

import com.baomidou.mybatisplus.extension.service.IService;
import ifl.agentbreaker.knowledgemanager.domain.dtos.requests.*;
import ifl.agentbreaker.knowledgemanager.domain.dtos.responses.PageResponse;
import ifl.agentbreaker.knowledgemanager.domain.dtos.responses.SyncTaskDetailResponse;
import ifl.agentbreaker.knowledgemanager.domain.dtos.responses.SyncTaskExecutionResponse;
import ifl.agentbreaker.knowledgemanager.domain.dtos.responses.SyncTaskResponse;
import ifl.agentbreaker.knowledgemanager.domain.entities.pg.SyncTask;
import jakarta.validation.Valid;
import stark.dataworks.boot.web.ServiceResponse;

public interface SyncTaskService extends IService<SyncTask>
{
    ServiceResponse<Boolean> createSyncTask(@Valid CreateSyncTaskRequest request);

    ServiceResponse<Boolean> updateSyncTask(@Valid UpdateSyncTaskRequest request);

    ServiceResponse<Boolean> deleteSyncTask(long syncTaskId);

    ServiceResponse<Boolean> updateSyncTaskEnableStatus(UpdateSyncTaskEnableStatusRequest request);

    ServiceResponse<SyncTaskDetailResponse> getSyncTaskDetail(long syncTaskId);

    ServiceResponse<PageResponse<SyncTaskExecutionResponse>> pageSyncTaskExecutions(@Valid PageSyncTaskExecutionsRequest request);

    ServiceResponse<PageResponse<SyncTaskResponse>> pageSyncTasks(@Valid PageSyncTasksRequest request);

    ServiceResponse<Boolean> executeSyncTask(long syncTaskId);
}
