package ifl.agentbreaker.knowledgemanager.services;


import com.aliyun.oss.model.OSSObject;
import jakarta.validation.constraints.NotBlank;

import java.io.InputStream;

public interface OssService
{
    String generateUploadUrl(String nameInOss, long fileSize, String contentType, long expireSeconds);

    OSSObject download(String nameInOss);

    boolean exists(@NotBlank String nameInOss);
}
