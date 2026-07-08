package ifl.agentbreaker.knowledgemanager.controllers;

import ifl.agentbreaker.knowledgemanager.domain.dtos.requests.PageEmbeddingTasksRequest;
import ifl.agentbreaker.knowledgemanager.domain.dtos.responses.EmbeddingTaskDetailResponse;
import ifl.agentbreaker.knowledgemanager.domain.dtos.responses.EmbeddingTaskResponse;
import ifl.agentbreaker.knowledgemanager.domain.dtos.responses.PageResponse;
import ifl.agentbreaker.knowledgemanager.services.EmbeddingTaskService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import stark.dataworks.boot.web.ServiceResponse;

@Slf4j
@RestController
@RequestMapping("/embedding-task")
public class EmbeddingTaskController
{
    @Autowired
    private EmbeddingTaskService service;

    /**
     * 查询单个Embedding任务详情
     * @param embeddingTaskId
     * @return
     */
    @GetMapping("/detail/{embeddingTaskId}")
    public ServiceResponse<EmbeddingTaskDetailResponse> getEmbeddingTaskDetail(@PathVariable long embeddingTaskId)
    {
        return service.getEmbeddingTaskDetail(embeddingTaskId);
    }

    /**
     * 分页查询Embedding任务列表
     * @param request
     * @return
     */
    @GetMapping("/page")
    public ServiceResponse<PageResponse<EmbeddingTaskResponse>> pageEmbeddingTasks(@Valid PageEmbeddingTasksRequest request)
    {
        return service.pageEmbeddingTasks(request);
    }

    /**
     * 重试Embedding任务（失败任务）
     * @param embeddingTaskId
     * @return
     */
    @PostMapping("/retry/{embeddingTaskId}")
    public ServiceResponse<Boolean> retryEmbeddingTask(@PathVariable long embeddingTaskId)
    {
        return service.retryEmbeddingTask(embeddingTaskId);
    }
}
