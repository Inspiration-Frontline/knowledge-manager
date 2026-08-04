package ifl.agentbreaker.knowledgemanager.exception;

import lombok.Getter;

@Getter
public enum KnowledgeManagerBusinessError
{
    ERROR_INTERNAL(10001, "Internal server error."),

    ERROR_BAD_REQUEST(20001, "Bad request."),

    KNOWLEDGE_BASE_ALREADY_EXISTS(30001, "Knowledge base already exists."),
    KNOWLEDGE_BASE_NOT_EXISTS(30002, "Knowledge base doesn't exist."),

    CRAWL_TASK_ALREADY_EXISTS(40001, "Crawl task already exists."),
    CRAWL_TASK_NOT_EXISTS(40002, "Crawl task doesn't exist."),


    ;

    private final int code;

    private final String message;

    KnowledgeManagerBusinessError(int code, String message)
    {
        this.code = code;
        this.message = message;
    }
}
