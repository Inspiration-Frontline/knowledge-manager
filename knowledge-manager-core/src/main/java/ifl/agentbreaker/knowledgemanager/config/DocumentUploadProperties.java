package ifl.agentbreaker.knowledgemanager.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "document.upload")
public class DocumentUploadProperties
{
    private int maxCount = 10;

    private long urlExpirationSeconds = 3600;
}
