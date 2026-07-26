package ifl.agentbreaker.knowledgemanager.services.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import ifl.agentbreaker.knowledgemanager.domain.dtos.requests.*;
import ifl.agentbreaker.knowledgemanager.domain.dtos.responses.CrawlTaskExecutionResponse;
import ifl.agentbreaker.knowledgemanager.domain.dtos.responses.CrawlTaskDetailResponse;
import ifl.agentbreaker.knowledgemanager.domain.dtos.responses.CrawlTaskResponse;
import ifl.agentbreaker.knowledgemanager.domain.dtos.responses.PageResponse;
import ifl.agentbreaker.knowledgemanager.domain.entities.pg.CrawlTask;
import ifl.agentbreaker.knowledgemanager.mappers.CrawlTaskMapper;
import ifl.agentbreaker.knowledgemanager.services.CrawlTaskService;
import org.springframework.stereotype.Service;
import stark.dataworks.boot.web.ServiceResponse;

@Service
public class CrawlTaskServiceImpl extends ServiceImpl<CrawlTaskMapper, CrawlTask> implements CrawlTaskService
{
    @Override
    public ServiceResponse<Long> createCrawlTask(CreateCrawlTaskRequest request)
    {
        return null;
    }

    @Override
    public ServiceResponse<Boolean> updateCrawlTask(UpdateCrawlTaskRequest request)
    {
        return null;
    }

    @Override
    public ServiceResponse<Boolean> deleteCrawlTask(long crawlTaskId)
    {
        return null;
    }

    @Override
    public ServiceResponse<Boolean> updateCrawlTaskStatus(UpdateCrawlTaskEnableStatusRequest request)
    {
        return null;
    }

    @Override
    public ServiceResponse<CrawlTaskDetailResponse> getCrawlTaskDetail(long crawlTaskId)
    {
        return null;
    }

    @Override
    public ServiceResponse<PageResponse<CrawlTaskExecutionResponse>> pageCrawlTaskExecutions(PageCrawlTaskExecutionsRequest request)
    {
        return null;
    }

    @Override
    public ServiceResponse<PageResponse<CrawlTaskResponse>> pageCrawlTasks(PageCrawlTasksRequest request)
    {
        return null;
    }

    @Override
    public ServiceResponse<Boolean> executeCrawlTask(long crawlTaskId)
    {
        return null;
    }
}
