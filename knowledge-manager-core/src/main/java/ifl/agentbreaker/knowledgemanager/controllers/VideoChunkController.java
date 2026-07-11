package ifl.agentbreaker.knowledgemanager.controllers;

import ifl.agentbreaker.knowledgemanager.domain.dtos.requests.ImportVideoRequest;
import ifl.agentbreaker.knowledgemanager.domain.dtos.requests.PageVideosRequest;
import ifl.agentbreaker.knowledgemanager.domain.dtos.responses.PageResponse;
import ifl.agentbreaker.knowledgemanager.domain.dtos.responses.VideoDetailResponse;
import ifl.agentbreaker.knowledgemanager.domain.dtos.responses.VideoResponse;
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
    private VideoChunkService service;

    /**
     * 导入视频
     * @param request
     * @return
     */
    @PostMapping("/import")
    public ServiceResponse<Long> importVideo(@RequestBody @Valid ImportVideoRequest request)
    {
        return service.importVideo(request);
    }

    /**
     * 删除视频
     * @param videoId
     * @return
     */
    @DeleteMapping("/{videoId}")
    // knowledgeBaseId
    // videoId
    public ServiceResponse<Boolean> deleteVideo(@PathVariable long videoId)
    {
        return service.deleteVideo(videoId);
    }

    /**
     * 查询单个视频详情
     * @param videoId
     * @return
     */
    @GetMapping("/detail/{videoId}")
    public ServiceResponse<VideoDetailResponse> getVideoDetail(@PathVariable long videoId)
    {
        return service.getVideoDetail(videoId);
    }

    /**
     * 分页查询视频列表
     * @param request
     * @return
     */
    @GetMapping("/page")
    public ServiceResponse<PageResponse<VideoResponse>> pageVideos(@Valid PageVideosRequest request)
    {
        return service.pageVideos(request);
    }
}
