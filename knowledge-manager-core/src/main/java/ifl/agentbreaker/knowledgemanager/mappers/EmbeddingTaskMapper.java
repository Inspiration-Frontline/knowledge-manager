package ifl.agentbreaker.knowledgemanager.mappers;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import ifl.agentbreaker.knowledgemanager.domain.entities.pg.EmbeddingTask;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EmbeddingTaskMapper extends BaseMapper<EmbeddingTask>
{
}
