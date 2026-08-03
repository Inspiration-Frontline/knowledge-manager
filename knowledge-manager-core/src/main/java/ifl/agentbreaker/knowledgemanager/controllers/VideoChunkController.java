package ifl.agentbreaker.knowledgemanager.controllers;

import ifl.agentbreaker.knowledgemanager.domain.dtos.requests.ImportVideoRequest;
import ifl.agentbreaker.knowledgemanager.domain.dtos.requests.PageVideoChunksRequest;
import ifl.agentbreaker.knowledgemanager.domain.dtos.responses.*;
import ifl.agentbreaker.knowledgemanager.services.VideoChunkService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import stark.dataworks.boot.web.ServiceResponse;

@Slf4j
@RestController
@RequestMapping("/video-chunk")
public class VideoChunkController
{
    @Autowired
    private VideoChunkService videoChunkService;

    /**
     * 导入视频
     * @param request
     * @return
     */
    @PostMapping("/import")
    public ServiceResponse<Boolean> importVideo(@RequestBody @Valid ImportVideoRequest request)
    {
        return videoChunkService.importVideo(request);
    }

    /**
     * 删除视频chunk
     * @param chunkId
     * @return
     */
    @DeleteMapping("/{chunkId}")
    public ServiceResponse<Boolean> deleteVideoChunk(@PathVariable long chunkId)
    {
        return videoChunkService.deleteVideoChunk(chunkId);
    }

    /**
     * 分页查询视频chunk列表
     * @param request
     * @return
     */
    @GetMapping("/page")
    public ServiceResponse<PageResponse<VideoChunkAbstract>> pageVideoChunks(@ModelAttribute @Valid PageVideoChunksRequest request)
    {
        return videoChunkService.pageVideoChunks(request);
    }

    /**
     * 查询视频chunk详情
     * @param chunkId
     * @return
     */
    @GetMapping("/detail/{chunkId}")
    public ServiceResponse<VideoChunkDetail> getVideoChunkDetail(@PathVariable long chunkId)
    {
        return videoChunkService.getVideoChunkDetail(chunkId);
    }
}
