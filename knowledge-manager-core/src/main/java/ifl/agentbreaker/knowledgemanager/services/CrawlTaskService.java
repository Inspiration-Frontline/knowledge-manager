package ifl.agentbreaker.knowledgemanager.services;

import com.baomidou.mybatisplus.extension.service.IService;
import ifl.agentbreaker.knowledgemanager.domain.dtos.requests.*;
import ifl.agentbreaker.knowledgemanager.domain.dtos.responses.CrawlTaskExecutionResponse;
import ifl.agentbreaker.knowledgemanager.domain.dtos.responses.CrawlTaskDetailResponse;
import ifl.agentbreaker.knowledgemanager.domain.dtos.responses.CrawlTaskResponse;
import ifl.agentbreaker.knowledgemanager.domain.dtos.responses.PageResponse;
import ifl.agentbreaker.knowledgemanager.domain.entities.pg.CrawlTask;
import jakarta.validation.Valid;
import stark.dataworks.boot.web.ServiceResponse;

public interface CrawlTaskService extends IService<CrawlTask>
{
    ServiceResponse<Long> createCrawlTask(@Valid CreateCrawlTaskRequest request);

    ServiceResponse<Boolean> updateCrawlTask(@Valid UpdateCrawlTaskRequest request);

    ServiceResponse<Boolean> deleteCrawlTask(long crawlTaskId);

    ServiceResponse<Boolean> updateCrawlTaskStatus(UpdateCrawlTaskEnableStatusRequest request);

    ServiceResponse<CrawlTaskDetailResponse> getCrawlTaskDetail(long crawlTaskId);

    ServiceResponse<PageResponse<CrawlTaskExecutionResponse>> pageCrawlTaskExecutions(PageCrawlTaskExecutionsRequest request);

    ServiceResponse<PageResponse<CrawlTaskResponse>> pageCrawlTasks(@Valid PageCrawlTasksRequest request);

    ServiceResponse<Boolean> executeCrawlTask(long crawlTaskId);
}
