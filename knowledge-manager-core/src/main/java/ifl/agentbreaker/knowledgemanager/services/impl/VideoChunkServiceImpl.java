package ifl.agentbreaker.knowledgemanager.services.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import ifl.agentbreaker.knowledgemanager.mappers.VideoMapper;
import ifl.agentbreaker.knowledgemanager.services.VideoChunkService;
import org.springframework.stereotype.Service;

@Service
public class VideoChunkServiceImpl extends ServiceImpl<VideoMapper, Video> implements VideoChunkService
{
}
