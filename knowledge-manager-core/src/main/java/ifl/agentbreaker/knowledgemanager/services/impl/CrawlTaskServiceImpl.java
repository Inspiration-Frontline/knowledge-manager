package ifl.agentbreaker.knowledgemanager.services.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import ifl.agentbreaker.knowledgemanager.domain.dtos.requests.*;
import ifl.agentbreaker.knowledgemanager.domain.dtos.responses.CrawlTaskExecutionDetail;
import ifl.agentbreaker.knowledgemanager.domain.dtos.responses.CrawlTaskDetail;
import ifl.agentbreaker.knowledgemanager.domain.dtos.responses.CrawlTaskAbstract;
import ifl.agentbreaker.knowledgemanager.domain.dtos.responses.PageResponse;
import ifl.agentbreaker.knowledgemanager.domain.entities.pg.CrawlTask;
import ifl.agentbreaker.knowledgemanager.domain.entities.pg.KnowledgeBaseMetadata;
import ifl.agentbreaker.knowledgemanager.exception.KnowledgeManagerBusinessError;
import ifl.agentbreaker.knowledgemanager.mappers.CrawlTaskMapper;
import ifl.agentbreaker.knowledgemanager.mappers.KnowledgeBaseMapper;
import ifl.agentbreaker.knowledgemanager.services.CrawlTaskService;
import ifl.agentbreaker.knowledgemanager.utils.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import stark.dataworks.boot.web.ServiceResponse;

import java.util.Locale;

@Service
public class CrawlTaskServiceImpl extends ServiceImpl<CrawlTaskMapper, CrawlTask> implements CrawlTaskService
{
    @Autowired
    private CrawlTaskMapper crawlTaskMapper;

    @Autowired
    private KnowledgeBaseMapper knowledgeBaseMapper;

    @Override
    public ServiceResponse<Boolean> createCrawlTask(CreateCrawlTaskRequest request)
    {
        // Validate request parameters.
        if (request.getKnowledgeBaseId() <= 0 ||
                request.getStartUrls()
                       .stream()
                       .anyMatch(url -> url == null || url.isBlank()))
        {
            return ServiceResponse.buildErrorResponse(
                    KnowledgeManagerBusinessError.ERROR_BAD_REQUEST.getCode(),
                    KnowledgeManagerBusinessError.ERROR_BAD_REQUEST.getMessage()
            );
        }

        // Validate whether knowledge base already exists.
        KnowledgeBaseMetadata knowledgeBaseMetadata = knowledgeBaseMapper.selectById(request.getKnowledgeBaseId());
        if (knowledgeBaseMetadata == null)
        {
            return ServiceResponse.buildErrorResponse(
                    KnowledgeManagerBusinessError.KNOWLEDGE_BASE_NOT_EXISTS.getCode(),
                    KnowledgeManagerBusinessError.KNOWLEDGE_BASE_NOT_EXISTS.getMessage()
            );
        }

        // Validate whether crawl task already exists.
        Long count = crawlTaskMapper.selectCount(Wrappers.lambdaQuery(CrawlTask.class)
                                                         .eq(CrawlTask::getKnowledgeBaseId, request.getKnowledgeBaseId())
                                                         .eq(CrawlTask::getTaskName, request.getTaskName()
                                                                                            .strip()));
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
        crawlTask.setTaskName(request.getTaskName()
                                     .strip());
        crawlTask.setStartUrls(request.getStartUrls()
                                      .stream()
                                      .map(String::strip)
                                      .toList());
        crawlTask.setCronExpression(request.getCronExpression()
                                           .strip());
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
        // Validate request parameters.
        if (request.getTaskName() == null && request.getStartUrls() == null &&
                request.getCronExpression() == null && request.getMaxDepth() == null)
        {
            return ServiceResponse.buildErrorResponse(
                    KnowledgeManagerBusinessError.ERROR_BAD_REQUEST.getCode(),
                    KnowledgeManagerBusinessError.ERROR_BAD_REQUEST.getMessage()
            );
        }
        if (request.getCrawlTaskId() <= 0 ||
                (request.getTaskName() != null && request.getTaskName()
                                                         .isBlank()) ||
                request.getStartUrls() != null && request.getStartUrls()
                                                         .stream()
                                                         .anyMatch(url -> url == null || url.isBlank()) ||
                request.getCronExpression() != null && request.getCronExpression()
                                                              .isBlank())
        {
            return ServiceResponse.buildErrorResponse(
                    KnowledgeManagerBusinessError.ERROR_BAD_REQUEST.getCode(),
                    KnowledgeManagerBusinessError.ERROR_BAD_REQUEST.getMessage()
            );
        }

        // Validate whether it already exists.
        CrawlTask crawlTask = crawlTaskMapper.selectOne(Wrappers.lambdaQuery(CrawlTask.class)
                                                                .eq(CrawlTask::getId, request.getCrawlTaskId()));
        if (crawlTask == null)
        {
            return ServiceResponse.buildErrorResponse(
                    KnowledgeManagerBusinessError.CRAWL_TASK_NOT_EXISTS.getCode(),
                    KnowledgeManagerBusinessError.CRAWL_TASK_NOT_EXISTS.getMessage()
            );
        }

        // Update crawl task.
        CrawlTask newCrawlTask = new CrawlTask();
        newCrawlTask.setId(request.getCrawlTaskId());
        if (request.getTaskName() != null)
        {
            // Validate new crawl task is not existing.
            Long count = crawlTaskMapper.selectCount(Wrappers.lambdaQuery(CrawlTask.class)
                                                             .eq(CrawlTask::getKnowledgeBaseId, crawlTask.getKnowledgeBaseId())
                                                             .eq(CrawlTask::getTaskName, request.getTaskName()
                                                                                                .strip())
                                                             .ne(CrawlTask::getId, request.getCrawlTaskId()));
            if (count > 0)
            {
                return ServiceResponse.buildErrorResponse(
                        KnowledgeManagerBusinessError.CRAWL_TASK_ALREADY_EXISTS.getCode(),
                        KnowledgeManagerBusinessError.CRAWL_TASK_ALREADY_EXISTS.getMessage()
                );
            }
            newCrawlTask.setTaskName(request.getTaskName()
                                            .strip());
        }

        if (request.getStartUrls() != null)
            newCrawlTask.setStartUrls(request.getStartUrls()
                                             .stream()
                                             .map(String::strip)
                                             .toList());
        if (request.getCronExpression() != null)
            newCrawlTask.setCronExpression(request.getCronExpression()
                                                  .strip());
        if (request.getMaxDepth() != null)
            newCrawlTask.setMaxDepth(request.getMaxDepth());
        else
            newCrawlTask.setMaxDepth(crawlTask.getMaxDepth());
        newCrawlTask.setModifierId(UserContext.getCurrentUserId());
        crawlTaskMapper.updateById(newCrawlTask);

        return ServiceResponse.buildSuccessResponse(true);
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
