package ifl.agentbreaker.knowledgemanager.services;

import com.baomidou.mybatisplus.extension.service.IService;
import ifl.agentbreaker.knowledgemanager.domain.dtos.requests.*;
import ifl.agentbreaker.knowledgemanager.domain.dtos.responses.KnowledgeBaseDetail;
import ifl.agentbreaker.knowledgemanager.domain.dtos.responses.KnowledgeBaseAbstract;
import ifl.agentbreaker.knowledgemanager.domain.dtos.responses.PageResponse;
import ifl.agentbreaker.knowledgemanager.domain.entities.pg.KnowledgeBaseMetadata;
import jakarta.validation.Valid;
import stark.dataworks.boot.web.ServiceResponse;

public interface KnowledgeBaseService extends IService<KnowledgeBaseMetadata>
{
    ServiceResponse<Boolean> createKnowledgeBase(@Valid CreateKnowledgeBaseRequest request);

    ServiceResponse<Boolean> updateKnowledgeBase(@Valid UpdateKnowledgeBaseRequest request);

    ServiceResponse<Boolean> updateDocumentKnowledgeBase(UpdateDocumentKnowledgeBaseRequest request);

    ServiceResponse<Boolean> updateDocumentImageKnowledgeBase(UpdateDocumentImageKnowledgeBaseRequest request);

    ServiceResponse<Boolean> deleteKnowledgeBase(long knowledgeBaseId);

    ServiceResponse<Boolean> updateKnowledgeBaseEnableStatus(UpdateKnowledgeBaseEnableStatusRequest request);

    ServiceResponse<KnowledgeBaseDetail> getKnowledgeBaseDetail(long knowledgeBaseId);

    ServiceResponse<PageResponse<KnowledgeBaseAbstract>> pageKnowledgeBases(@Valid PageKnowledgeBasesRequest request);
}
