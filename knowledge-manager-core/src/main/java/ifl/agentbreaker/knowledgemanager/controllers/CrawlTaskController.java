package ifl.agentbreaker.knowledgemanager.controllers;

import ifl.agentbreaker.knowledgemanager.domain.constants.EnableStatus;
import ifl.agentbreaker.knowledgemanager.domain.dtos.requests.CreateCrawlTaskRequest;
import ifl.agentbreaker.knowledgemanager.domain.dtos.requests.PageCrawlTasksRequest;
import ifl.agentbreaker.knowledgemanager.domain.dtos.requests.UpdateCrawlTaskRequest;
import ifl.agentbreaker.knowledgemanager.domain.dtos.responses.CrawlTaskDetailResponse;
import ifl.agentbreaker.knowledgemanager.domain.dtos.responses.CrawlTaskResponse;
import ifl.agentbreaker.knowledgemanager.domain.dtos.responses.PageResponse;
import ifl.agentbreaker.knowledgemanager.services.CrawlTaskService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import stark.dataworks.boot.web.ServiceResponse;

@Slf4j
@RestController
@RequestMapping("/crawl-task")
public class CrawlTaskController
{
    @Autowired
    private CrawlTaskService service;

    /**
     * 创建爬虫任务
     * @param request
     * @return
     */
    @PostMapping("/create")
    public ServiceResponse<Long> createCrawlTask(@RequestBody @Valid CreateCrawlTaskRequest request)
    {
        return service.createCrawlTask(request);
    }

    /**
     * 修改爬虫任务
     * @param request
     * @return
     */
    @PutMapping("/update")
    public ServiceResponse<Boolean> updateCrawlTask(@RequestBody @Valid UpdateCrawlTaskRequest request)
    {
        return service.updateCrawlTask(request);
    }

    /**
     * 删除爬虫任务
     * @param crawlTaskId
     * @return
     */
    @DeleteMapping("/delete/{crawlTaskId}")
    public ServiceResponse<Boolean> deleteCrawlTask(@PathVariable long crawlTaskId)
    {
        return service.deleteCrawlTask(crawlTaskId);
    }

    /**
     * 修改爬虫任务状态（启用/禁用）
     * @param enableStatus
     * @param crawlTaskId
     * @return
     */
    // TODO: Combine enableStatus & crawlTaskId to 1 single class.
    @PutMapping("/enable-status")
    public ServiceResponse<Boolean> updateCrawlTaskEnableStatus(@RequestParam EnableStatus enableStatus, @PathVariable long crawlTaskId)
    {
        return service.updateCrawlTaskStatus(enableStatus, crawlTaskId);
    }

    /**
     * 查询单个爬虫任务详情
     * @param crawlTaskId
     * @return
     */
    @GetMapping("/detail/{crawlTaskId}")
    public ServiceResponse<CrawlTaskDetailResponse> getCrawlTaskDetail(@PathVariable long crawlTaskId)
    {
        return service.getCrawlTaskDetail(crawlTaskId);
    }

    // /detail/executions
    // paginated

    /**
     * 分页查询爬虫任务列表
     * @param request
     * @return
     */
    @GetMapping("/page")
    public ServiceResponse<PageResponse<CrawlTaskResponse>> pageCrawlTasks(@Valid PageCrawlTasksRequest request)
    {
        return service.pageCrawlTasks(request);
    }

    // TODO: Add a button on the frontend.
    /**
     * 立即执行一次爬虫任务
     * @param crawlTaskId
     * @return
     */
    @PostMapping("/execute/{crawlTaskId}")
    public ServiceResponse<Boolean> executeCrawlTask(@PathVariable long crawlTaskId)
    {
        return service.executeCrawlTask(crawlTaskId);
    }
}
