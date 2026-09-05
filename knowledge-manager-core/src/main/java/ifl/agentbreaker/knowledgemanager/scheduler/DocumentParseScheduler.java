package ifl.agentbreaker.knowledgemanager.scheduler;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import ifl.agentbreaker.knowledgemanager.domain.constants.ParsingStatus;
import ifl.agentbreaker.knowledgemanager.domain.constants.UploadStatus;
import ifl.agentbreaker.knowledgemanager.domain.entities.pg.Document;
import ifl.agentbreaker.knowledgemanager.mappers.DocumentMapper;
import ifl.agentbreaker.knowledgemanager.services.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DocumentParseScheduler
{
    @Autowired
    private DocumentMapper documentMapper;

    @Autowired
    private DocumentService documentService;

    /**
     * Scan the documents to be parsed every 30 seconds.
     */
    @Scheduled(fixedDelay = 30000)
    public void scanAndParseUnparsedDocuments()
    {

        List<Document> documents = documentMapper.selectList(Wrappers.lambdaQuery(Document.class)
                                                                     // OSS upload successfully.
                                                                     .eq(Document::getUploadStatus, UploadStatus.SUCCESS)
                                                                     // Not yet parsed.
                                                                     .eq(Document::getParsingStatus, ParsingStatus.UNPARSED)
                                                                     .last("limit 100"));

        for (Document document : documents)
        {
            // Seize tasks to prevent repetitive parsing.(Optimistic locking)
            int locked = documentMapper.update(Wrappers.lambdaUpdate(Document.class)
                                                       .set(Document::getParsingStatus, ParsingStatus.PARSING)
                                                       .set(Document::getModifierId, -1)
                                                       .eq(Document::getId, document.getId())
                                                       .eq(Document::getParsingStatus, ParsingStatus.UNPARSED));
            if (locked == 0)
            {
                continue;
            }

            try
            {
                documentService.parseDocument(document.getId());
            }
            catch (Exception e)
            {
                documentMapper.update(Wrappers.lambdaUpdate(Document.class)
                                              .set(Document::getParsingStatus, ParsingStatus.FAILED)
                                              .set(Document::getModifierId, -1)
                                              .eq(Document::getId, document.getId()));
            }
        }
    }
}
