package ifl.agentbreaker.knowledgemanager.exception;

import lombok.Getter;

@Getter
public class ServiceResponseException extends RuntimeException
{
    private final int code;

    public ServiceResponseException(KnowledgeManagerBusinessError knowledgeManagerBusinessError)
    {
        super(knowledgeManagerBusinessError.getMessage());
        this.code = knowledgeManagerBusinessError.getCode();
    }
}
