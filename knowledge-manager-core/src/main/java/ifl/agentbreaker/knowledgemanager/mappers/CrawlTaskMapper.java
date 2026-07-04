package ifl.agentbreaker.knowledgemanager.mappers;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import ifl.agentbreaker.knowledgemanager.domain.entities.pg.CrawlTask;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CrawlTaskMapper extends BaseMapper<CrawlTask>
{
}
