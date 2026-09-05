package ifl.agentbreaker.knowledgemanager.domain.dtos.document;

import lombok.Data;

@Data
public class ParsedImage
{

    /**
     * 图片二进制
     * 上传OSS前存在这里
     */
    private byte[] data;

    /**
     * 图片宽
     */
    private int width;

    /**
     * 图片高
     */
    private int height;

    /**
     * 原始caption
     * 来自文档
     * 没有则null
     */
    private String description;

    /**
     * 图片在OSS中的名字
     * 上传后补充
     */
    private String nameInOss;
}