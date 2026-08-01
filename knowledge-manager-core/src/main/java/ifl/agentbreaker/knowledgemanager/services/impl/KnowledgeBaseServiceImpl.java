package ifl.agentbreaker.knowledgemanager.services.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import ifl.agentbreaker.knowledgemanager.domain.dtos.requests.CreateKnowledgeBaseRequest;
import ifl.agentbreaker.knowledgemanager.domain.dtos.requests.PageKnowledgeBasesRequest;
import ifl.agentbreaker.knowledgemanager.domain.dtos.requests.UpdateKnowledgeBaseEnableStatusRequest;
import ifl.agentbreaker.knowledgemanager.domain.dtos.requests.UpdateKnowledgeBaseRequest;
import ifl.agentbreaker.knowledgemanager.domain.dtos.responses.KnowledgeBaseDetailResponse;
import ifl.agentbreaker.knowledgemanager.domain.dtos.responses.KnowledgeBaseResponse;
import ifl.agentbreaker.knowledgemanager.domain.dtos.responses.PageResponse;
import ifl.agentbreaker.knowledgemanager.domain.entities.pg.KnowledgeBaseMetadata;
import ifl.agentbreaker.knowledgemanager.mappers.KnowledgeBaseMapper;
import ifl.agentbreaker.knowledgemanager.services.KnowledgeBaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import stark.dataworks.boot.web.ServiceResponse;

@Service
public class KnowledgeBaseServiceImpl extends ServiceImpl<KnowledgeBaseMapper, KnowledgeBaseMetadata> implements KnowledgeBaseService
{
    @Autowired
    private KnowledgeBaseMapper knowledgeBaseMapper;

    @Override
    public ServiceResponse<Boolean> createKnowledgeBase(CreateKnowledgeBaseRequest request)
    {
        return null;
    }

    @Override
    public ServiceResponse<Boolean> updateKnowledgeBase(UpdateKnowledgeBaseRequest request)
    {
        return null;
    }

    @Override
    public ServiceResponse<Boolean> deleteKnowledgeBase(long knowledgeBaseId)
    {
        return null;
    }

    @Override
    public ServiceResponse<Boolean> updateKnowledgeBaseEnableStatus(UpdateKnowledgeBaseEnableStatusRequest request)
    {
        return null;
    }

    @Override
    public ServiceResponse<KnowledgeBaseDetailResponse> getKnowledgeBaseDetail(long knowledgeBaseId)
    {
        return null;
    }

    @Override
    public ServiceResponse<PageResponse<KnowledgeBaseResponse>> pageKnowledgeBases(PageKnowledgeBasesRequest request)
    {
        return null;
    }
}
