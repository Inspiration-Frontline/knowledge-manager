package ifl.agentbreaker.knowledgemanager.services.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import ifl.agentbreaker.knowledgemanager.domain.dtos.requests.*;
import ifl.agentbreaker.knowledgemanager.domain.dtos.responses.CrawlTaskExecutionDetail;
import ifl.agentbreaker.knowledgemanager.domain.dtos.responses.CrawlTaskDetail;
import ifl.agentbreaker.knowledgemanager.domain.dtos.responses.CrawlTaskAbstract;
import ifl.agentbreaker.knowledgemanager.domain.dtos.responses.PageResponse;
import ifl.agentbreaker.knowledgemanager.domain.entities.pg.CrawlTask;
import ifl.agentbreaker.knowledgemanager.mappers.CrawlTaskMapper;
import ifl.agentbreaker.knowledgemanager.services.CrawlTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import stark.dataworks.boot.web.ServiceResponse;

@Service
public class CrawlTaskServiceImpl extends ServiceImpl<CrawlTaskMapper, CrawlTask> implements CrawlTaskService
{
    @Autowired
    private CrawlTaskMapper crawlTaskMapper;

    @Override
    public ServiceResponse<Boolean> createCrawlTask(CreateCrawlTaskRequest request)
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
    public ServiceResponse<Boolean> updateCrawlTaskEnableStatus(UpdateCrawlTaskEnableStatusRequest request)
    {
        return null;
    }

    @Override
    public ServiceResponse<CrawlTaskDetail> getCrawlTaskDetail(long crawlTaskId)
    {
        return null;
    }

    @Override
    public ServiceResponse<PageResponse<CrawlTaskExecutionDetail>> pageCrawlTaskExecutions(PageCrawlTaskExecutionsRequest request)
    {
        return null;
    }

    @Override
    public ServiceResponse<PageResponse<CrawlTaskAbstract>> pageCrawlTasks(PageCrawlTasksRequest request)
    {
        return null;
    }

    @Override
    public ServiceResponse<Boolean> executeCrawlTask(long crawlTaskId)
    {
        return null;
    }
}
