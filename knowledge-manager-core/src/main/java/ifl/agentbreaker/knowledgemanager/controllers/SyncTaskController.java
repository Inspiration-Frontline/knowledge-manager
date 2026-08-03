package ifl.agentbreaker.knowledgemanager.controllers;

import ifl.agentbreaker.knowledgemanager.domain.dtos.requests.*;
import ifl.agentbreaker.knowledgemanager.domain.dtos.responses.*;
import ifl.agentbreaker.knowledgemanager.services.SyncTaskService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import stark.dataworks.boot.web.ServiceResponse;


@Slf4j
@RestController
@RequestMapping("/sync-task")
public class SyncTaskController
{
    @Autowired
    private SyncTaskService syncTaskService;

    /**
     * 创建同步任务
     * @param request
     * @return
     */
    @PostMapping("/create")
    public ServiceResponse<Boolean> createSyncTask(@RequestBody @Valid CreateSyncTaskRequest request)
    {
        return syncTaskService.createSyncTask(request);
    }

    /**
     * 修改同步任务
     * @param request
     * @return
     */
    @PutMapping("/update")
    public ServiceResponse<Boolean> updateSyncTask(@RequestBody @Valid UpdateSyncTaskRequest request)
    {
        return syncTaskService.updateSyncTask(request);
    }

    /**
     * 删除同步任务
     * @param syncTaskId
     * @return
     */
    @DeleteMapping("/{syncTaskId}")
    public ServiceResponse<Boolean> deleteSyncTask(@PathVariable long syncTaskId)
    {
        return syncTaskService.deleteSyncTask(syncTaskId);
    }

    /**
     * 修改同步任务状态（启用/禁用）
     * @param request
     * @return
     */
    @PutMapping("/enable-status")
    public ServiceResponse<Boolean> updateSyncTaskEnableStatus(@RequestBody UpdateSyncTaskEnableStatusRequest request)
    {
        return syncTaskService.updateSyncTaskEnableStatus(request);
    }

    /**
     * 查询单个同步任务详情
     * @param syncTaskId
     * @return
     */
    @GetMapping("/detail/{syncTaskId}")
    public ServiceResponse<SyncTaskDetail> getSyncTaskDetail(@PathVariable long syncTaskId)
    {
        return syncTaskService.getSyncTaskDetail(syncTaskId);
    }

    /**
     * 分页查询单个同步任务的执行记录
     * @param request
     * @return
     */
    @GetMapping("/executions")
    public ServiceResponse<PageResponse<SyncTaskExecutionDetail>> pageSyncTaskExecutions(@ModelAttribute @Valid PageSyncTaskExecutionsRequest request)
    {
        return syncTaskService.pageSyncTaskExecutions(request);
    }

    /**
     * 分页查询同步任务列表
     * @param request
     * @return
     */
    @GetMapping("/page")
    public ServiceResponse<PageResponse<SyncTaskAbstract>> pageSyncTasks(@ModelAttribute @Valid PageSyncTasksRequest request)
    {
        return syncTaskService.pageSyncTasks(request);
    }

    // TODO: Add a button on the frontend.
    /**
     * 立即执行一次同步任务
     * @param syncTaskId
     * @return
     */
    @PostMapping("/execute/{syncTaskId}")
    public ServiceResponse<Boolean> executeSyncTask(@PathVariable long syncTaskId)
    {
        return syncTaskService.executeSyncTask(syncTaskId);
    }
}
