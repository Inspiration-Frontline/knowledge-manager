package ifl.agentbreaker.knowledgemanager.services.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import ifl.agentbreaker.knowledgemanager.domain.entities.pg.Video;
import ifl.agentbreaker.knowledgemanager.mappers.VideoMapper;
import ifl.agentbreaker.knowledgemanager.services.VideoService;
import org.springframework.stereotype.Service;

@Service
public class VideoServiceImpl extends ServiceImpl<VideoMapper, Video> implements VideoService
{
}
