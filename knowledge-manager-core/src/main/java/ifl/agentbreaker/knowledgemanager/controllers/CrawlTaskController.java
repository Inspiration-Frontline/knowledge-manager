package ifl.agentbreaker.knowledgemanager.controllers;

import ifl.agentbreaker.knowledgemanager.domain.dtos.requests.*;
import ifl.agentbreaker.knowledgemanager.domain.dtos.responses.CrawlTaskExecutionDetail;
import ifl.agentbreaker.knowledgemanager.domain.dtos.responses.CrawlTaskDetail;
import ifl.agentbreaker.knowledgemanager.domain.dtos.responses.CrawlTaskAbstract;
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
    private CrawlTaskService crawlTaskService;

    /**
     * 创建爬虫任务
     * @param request
     * @return
     */
    @PostMapping("/create")
    public ServiceResponse<Boolean> createCrawlTask(@RequestBody @Valid CreateCrawlTaskRequest request)
    {
        return crawlTaskService.createCrawlTask(request);
    }

    /**
     * 修改爬虫任务
     * @param request
     * @return
     */
    @PutMapping("/update")
    public ServiceResponse<Boolean> updateCrawlTask(@RequestBody @Valid UpdateCrawlTaskRequest request)
    {
        return crawlTaskService.updateCrawlTask(request);
    }

    /**
     * 删除爬虫任务
     * @param crawlTaskId
     * @return
     */
    @DeleteMapping("/{crawlTaskId}")
    public ServiceResponse<Boolean> deleteCrawlTask(@PathVariable long crawlTaskId)
    {
        return crawlTaskService.deleteCrawlTask(crawlTaskId);
    }

    /**
     * 修改爬虫任务状态（启用/禁用）
     * @param request
     * @return
     */
    @PutMapping("/enable-status")
    public ServiceResponse<Boolean> updateCrawlTaskEnableStatus(@RequestBody UpdateCrawlTaskEnableStatusRequest request)
    {
        return crawlTaskService.updateCrawlTaskEnableStatus(request);
    }

    /**
     * 查询单个爬虫任务详情
     * @param crawlTaskId
     * @return
     */
    @GetMapping("/detail/{crawlTaskId}")
    public ServiceResponse<CrawlTaskDetail> getCrawlTaskDetail(@PathVariable long crawlTaskId)
    {
        return crawlTaskService.getCrawlTaskDetail(crawlTaskId);
    }

    /**
     * 分页查询单个爬虫任务的执行记录
     * @param request
     * @return
     */
    @GetMapping("/executions")
    public ServiceResponse<PageResponse<CrawlTaskExecutionDetail>> pageCrawlTaskExecutions(@ModelAttribute @Valid PageCrawlTaskExecutionsRequest request)
    {
        return crawlTaskService.pageCrawlTaskExecutions(request);
    }

    /**
     * 分页查询爬虫任务列表
     * @param request
     * @return
     */
    @GetMapping("/page")
    public ServiceResponse<PageResponse<CrawlTaskAbstract>> pageCrawlTasks(@ModelAttribute @Valid PageCrawlTasksRequest request)
    {
        return crawlTaskService.pageCrawlTasks(request);
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
        return crawlTaskService.executeCrawlTask(crawlTaskId);
    }
}
