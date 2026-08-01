package ifl.agentbreaker.knowledgemanager.controllers;

import ifl.agentbreaker.knowledgemanager.domain.dtos.requests.CreateKnowledgeBaseRequest;
import ifl.agentbreaker.knowledgemanager.domain.dtos.requests.UpdateKnowledgeBaseRequest;
import ifl.agentbreaker.knowledgemanager.domain.dtos.requests.PageKnowledgeBasesRequest;
import ifl.agentbreaker.knowledgemanager.domain.dtos.requests.UpdateKnowledgeBaseEnableStatusRequest;
import ifl.agentbreaker.knowledgemanager.domain.dtos.responses.KnowledgeBaseDetailResponse;
import ifl.agentbreaker.knowledgemanager.domain.dtos.responses.KnowledgeBaseResponse;
import ifl.agentbreaker.knowledgemanager.domain.dtos.responses.PageResponse;
import ifl.agentbreaker.knowledgemanager.services.KnowledgeBaseService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import stark.dataworks.boot.web.ServiceResponse;

@Slf4j
@RestController
@RequestMapping("/knowledge-base")
public class KnowledgeBaseController
{
    @Autowired
    private KnowledgeBaseService knowledgeBaseService;

    /**
     * 新增知识库
     * @param request
     * @return
     */
    @PostMapping("/create")
    public ServiceResponse<Boolean> createKnowledgeBase(@RequestBody @Valid CreateKnowledgeBaseRequest request)
    {
        return knowledgeBaseService.createKnowledgeBase(request);
    }

    /**
     * 修改知识库
     * @param request
     * @return
     */
    @PutMapping("/update")
    public ServiceResponse<Boolean> updateKnowledgeBase(@RequestBody @Valid UpdateKnowledgeBaseRequest request)
    {
        return knowledgeBaseService.updateKnowledgeBase(request);
    }

    /**
     * 删除知识库
     * @param knowledgeBaseId
     * @return
     */
    @DeleteMapping("/{knowledgeBaseId}")
    public ServiceResponse<Boolean> deleteKnowledgeBase(@PathVariable long knowledgeBaseId)
    {
        return knowledgeBaseService.deleteKnowledgeBase(knowledgeBaseId);
    }

    /**
     * 修改知识库状态（启用/禁用）
     * @param request
     * @return
     */
    @PutMapping("/enable-status")
    public ServiceResponse<Boolean> updateKnowledgeBaseEnableStatus(@RequestBody UpdateKnowledgeBaseEnableStatusRequest request)
    {
        return knowledgeBaseService.updateKnowledgeBaseEnableStatus(request);
    }

    /**
     * 查询单个知识库详情
     * @param knowledgeBaseId
     * @return
     */
    @GetMapping("/detail/{knowledgeBaseId}")
    public ServiceResponse<KnowledgeBaseDetailResponse> getKnowledgeBaseDetail(@PathVariable long knowledgeBaseId)
    {
        return knowledgeBaseService.getKnowledgeBaseDetail(knowledgeBaseId);
    }

    /**
     * 分页查询知识库列表
     * @param request
     * @return
     */
    @GetMapping("/page")
    public ServiceResponse<PageResponse<KnowledgeBaseResponse>> pageKnowledgeBases(@ModelAttribute @Valid PageKnowledgeBasesRequest request)
    {
        return knowledgeBaseService.pageKnowledgeBases(request);
    }
}
