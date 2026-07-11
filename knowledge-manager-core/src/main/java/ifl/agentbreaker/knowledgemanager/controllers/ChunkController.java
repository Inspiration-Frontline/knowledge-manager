package ifl.agentbreaker.knowledgemanager.controllers;

import ifl.agentbreaker.knowledgemanager.domain.dtos.requests.PageChunksRequest;
import ifl.agentbreaker.knowledgemanager.domain.dtos.requests.SearchChunksRequest;
import ifl.agentbreaker.knowledgemanager.domain.dtos.responses.ChunkDetailResponse;
import ifl.agentbreaker.knowledgemanager.domain.dtos.responses.ChunkAbstract;
import ifl.agentbreaker.knowledgemanager.domain.dtos.responses.ChunkSearchResponse;
import ifl.agentbreaker.knowledgemanager.domain.dtos.responses.PageResponse;
import ifl.agentbreaker.knowledgemanager.services.ChunkService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import stark.dataworks.boot.web.ServiceResponse;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/chunk")
public class ChunkController
{
    // TODO: DO NOT name it "service", name it "chunkService", same for other services.
    @Autowired
    private ChunkService service;

    /**
     * 查询单个Chunk详情
     * @param chunkId
     * @return
     */
    @GetMapping("/detail/{chunkId}")
    public ServiceResponse<ChunkDetailResponse> getChunkDetail(@PathVariable long chunkId)
    {
        return service.getChunkDetail(chunkId);
    }

    /**
     * 分页查询Chunk列表
     * @param request
     * @return
     */
    @GetMapping("/page")
    public ServiceResponse<PageResponse<ChunkAbstract>> getChunkAbstracts(@Valid PageChunksRequest request)
    {
        return service.getChunkAbstracts(request);
    }

    /**
     * 语义检索
     * @param request
     * @return
     */
    @PostMapping("/query")
    public ServiceResponse<List<ChunkSearchResponse>> queryChunks(@RequestBody @Valid SearchChunksRequest request)
    {
        return service.queryChunks(request);
    }
}
