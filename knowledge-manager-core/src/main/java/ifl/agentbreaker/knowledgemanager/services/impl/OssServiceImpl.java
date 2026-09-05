package ifl.agentbreaker.knowledgemanager.services.impl;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.HttpMethod;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.GeneratePresignedUrlRequest;
import com.aliyun.oss.model.OSSObject;
import ifl.agentbreaker.knowledgemanager.config.OssProperties;
import ifl.agentbreaker.knowledgemanager.services.OssService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.Date;

@Service
public class OssServiceImpl implements OssService
{
    @Autowired
    private OSS ossClient;

    @Autowired
    private OssProperties ossProperties;

    private static final String BUCKET_DOCUMENT_KEY = "document";

    @Override
    public String generateUploadUrl(String nameInOss, long fileSize, String contentType, long expireSeconds) throws OSSException, ClientException
    {
        String bucketName = ossProperties.getBuckets()
                                         .get(BUCKET_DOCUMENT_KEY);

        GeneratePresignedUrlRequest request = new GeneratePresignedUrlRequest(bucketName, nameInOss, HttpMethod.PUT);

        request.setExpiration(new Date(System.currentTimeMillis() + expireSeconds * 1000));

        request.setContentType(contentType);

        URL url = ossClient.generatePresignedUrl(request);

        return url.toString();
    }

    @Override
    public OSSObject download(String nameInOss) throws OSSException, ClientException
    {
        String bucketName = ossProperties.getBuckets()
                                         .get(BUCKET_DOCUMENT_KEY);

        return ossClient.getObject(bucketName, nameInOss);

    }

    @Override
    public boolean exists(String nameInOss)
    {
        String bucketName = ossProperties.getBuckets().get(BUCKET_DOCUMENT_KEY);
        return ossClient.doesObjectExist(bucketName, nameInOss);
    }
}
