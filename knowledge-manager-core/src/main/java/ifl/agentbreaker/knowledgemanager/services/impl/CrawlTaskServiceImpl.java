package ifl.agentbreaker.knowledgemanager.services.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import ifl.agentbreaker.knowledgemanager.domain.dtos.requests.*;
import ifl.agentbreaker.knowledgemanager.domain.dtos.responses.CrawlTaskExecutionDetail;
import ifl.agentbreaker.knowledgemanager.domain.dtos.responses.CrawlTaskDetail;
import ifl.agentbreaker.knowledgemanager.domain.dtos.responses.CrawlTaskAbstract;
import ifl.agentbreaker.knowledgemanager.domain.dtos.responses.PageResponse;
import ifl.agentbreaker.knowledgemanager.domain.entities.pg.CrawlTask;
import ifl.agentbreaker.knowledgemanager.exception.KnowledgeManagerBusinessError;
import ifl.agentbreaker.knowledgemanager.mappers.CrawlTaskMapper;
import ifl.agentbreaker.knowledgemanager.services.CrawlTaskService;
import ifl.agentbreaker.knowledgemanager.utils.UserContext;
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
        // Validate request parameters.
        if (request.getKnowledgeBaseId() <= 0)
        {
            return ServiceResponse.buildErrorResponse(
                    KnowledgeManagerBusinessError.ERROR_BAD_REQUEST.getCode(),
                    KnowledgeManagerBusinessError.ERROR_BAD_REQUEST.getMessage()
            );
        }

        // Validate whether it already exists.
        Long count = crawlTaskMapper.selectCount(Wrappers.lambdaQuery(CrawlTask.class)
                                                         .eq(CrawlTask::getKnowledgeBaseId, request.getKnowledgeBaseId())
                                                         .eq(CrawlTask::getTaskName, request.getTaskName()));
        if (count > 0)
        {
            return ServiceResponse.buildErrorResponse(
                    KnowledgeManagerBusinessError.CRAWL_TASK_ALREADY_EXISTS.getCode(),
                    KnowledgeManagerBusinessError.CRAWL_TASK_ALREADY_EXISTS.getMessage()
            );
        }

        // Insert crawl task into database.
        CrawlTask crawlTask = new CrawlTask();
        crawlTask.setKnowledgeBaseId(request.getKnowledgeBaseId());
        crawlTask.setTaskName(request.getTaskName());
        crawlTask.setStartUrls(request.getStartUrls());
        crawlTask.setCronExpression(request.getCronExpression());
        crawlTask.setMaxDepth(request.getMaxDepth());
        crawlTask.setEnabled(true);
        long userId = UserContext.getCurrentUserId();
        crawlTask.setCreatorId(userId);
        crawlTask.setModifierId(userId);
        crawlTaskMapper.insert(crawlTask);

        return ServiceResponse.buildSuccessResponse(true);
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
