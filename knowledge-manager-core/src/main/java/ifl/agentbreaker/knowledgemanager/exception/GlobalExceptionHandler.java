package ifl.agentbreaker.knowledgemanager.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import stark.dataworks.boot.web.ServiceResponse;


@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler
{

    @ExceptionHandler({
            MethodArgumentNotValidException.class,
            MethodArgumentTypeMismatchException.class,
            HttpMessageNotReadableException.class
    })
    public ServiceResponse<Boolean> handleBadRequestException(Exception e)
    {
        log.warn("Bad request.", e);
        return ServiceResponse.buildErrorResponse(
                KnowledgeManagerBusinessError.ERROR_BAD_REQUEST.getCode(),
                KnowledgeManagerBusinessError.ERROR_BAD_REQUEST.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ServiceResponse<Boolean> handleException(Exception e)
    {
        log.error("Unhandled exception.", e);
        return ServiceResponse.buildErrorResponse(
                KnowledgeManagerBusinessError.ERROR_INTERNAL.getCode(),
                KnowledgeManagerBusinessError.ERROR_INTERNAL.getMessage()
        );
    }
}
