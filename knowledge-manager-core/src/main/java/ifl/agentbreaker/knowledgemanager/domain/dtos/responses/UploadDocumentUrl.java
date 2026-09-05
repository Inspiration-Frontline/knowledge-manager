package ifl.agentbreaker.knowledgemanager.domain.dtos.responses;

import lombok.Data;

@Data
public class UploadDocumentUrl
{
    /**
     * 原文件名
     */
    private String fileName;

    /**
     * OSS对象名称
     */
    private String nameInOss;

    /**
     * 上传URL
     */
    private String uploadUrl;

    /**
     * 文件类型
     */
    private String contentType;
}
