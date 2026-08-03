package ifl.agentbreaker.knowledgemanager.controllers;

import ifl.agentbreaker.knowledgemanager.domain.dtos.requests.ImportImageRequest;
import ifl.agentbreaker.knowledgemanager.domain.dtos.requests.PageImageChunksRequest;
import ifl.agentbreaker.knowledgemanager.domain.dtos.responses.ImageChunkDetail;
import ifl.agentbreaker.knowledgemanager.domain.dtos.responses.ImageChunkAbstract;
import ifl.agentbreaker.knowledgemanager.domain.dtos.responses.PageResponse;
import ifl.agentbreaker.knowledgemanager.services.ImageChunkService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import stark.dataworks.boot.web.ServiceResponse;

@Slf4j
@RestController
@RequestMapping("/image-chunk")
public class ImageChunkController
{
    @Autowired
    private ImageChunkService imageChunkService;

    /**
     * 导入图片
     * @param request
     * @return
     */
    @PostMapping("/import")
    public ServiceResponse<Boolean> importImage(@RequestBody @Valid ImportImageRequest request)
    {
        return imageChunkService.importImage(request);
    }

    /**
     * 删除图片chunk
     * @param chunkId
     * @return
     */
    @DeleteMapping("/{chunkId}")
    public ServiceResponse<Boolean> deleteImageChunk(@PathVariable long chunkId)
    {
        return imageChunkService.deleteImageChunk(chunkId);
    }

    /**
     * 分页查询图片chunk列表
     * @param request
     * @return
     */
    @GetMapping("/page")
    public ServiceResponse<PageResponse<ImageChunkAbstract>> pageImageChunks(@ModelAttribute @Valid PageImageChunksRequest request)
    {
        return imageChunkService.pageImageChunks(request);
    }

    /**
     * 查询图片chunk详情
     * @param chunkId
     * @return
     */
    @GetMapping("/detail/{chunkId}")
    public ServiceResponse<ImageChunkDetail> getImageChunkDetail(@PathVariable long chunkId)
    {
        return imageChunkService.getImageChunkDetail(chunkId);
    }
}
