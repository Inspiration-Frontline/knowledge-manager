package ifl.agentbreaker.knowledgemanager.exception;

import lombok.Getter;

@Getter
public enum KnowledgeManagerBusinessError
{
    ERROR_INTERNAL(10001, "Internal server error."),

    ERROR_BAD_REQUEST(20001, "Bad request."),

    KNOWLEDGE_BASE_ALREADY_EXISTS(30001, "Knowledge base already exists."),
    KNOWLEDGE_BASE_NOT_EXISTS(30002, "Knowledge base doesn't exist."),

    DOCUMENT_READ_ERROR(40001, "Document reading failed."),
    DOCUMENT_UNSUPPORTED_TYPE(40002, "Unsupported document type."),
    DOCUMENT_ALREADY_EXISTS(40003, "Document already exists."),
    DOCUMENT_COUNT_EXCEEDS_LIMIT(40004, "Number of documents exceeds the maximum upload limit."),
    DOCUMENT_NOT_EXISTS(40005, "Document doesn't exist."),
    DOCUMENT_STATUS_ERROR(40006, "Document status error."),
    DOCUMENT_PARSING_ERROR(40007, "Document parsing error"),


    CRAWL_TASK_ALREADY_EXISTS(80001, "Crawl task already exists."),
    CRAWL_TASK_NOT_EXISTS(80002, "Crawl task doesn't exist."),

    OSS_UPLOAD_ERROR(90001, "Failed to upload the file to OSS."),


    ;

    private final int code;

    private final String message;

    KnowledgeManagerBusinessError(int code, String message)
    {
        this.code = code;
        this.message = message;
    }
}
