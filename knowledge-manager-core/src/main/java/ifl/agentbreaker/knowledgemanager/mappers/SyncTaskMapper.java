package ifl.agentbreaker.knowledgemanager.mappers;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import ifl.agentbreaker.knowledgemanager.domain.entities.pg.SyncTask;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SyncTaskMapper extends BaseMapper<SyncTask>
{
}
