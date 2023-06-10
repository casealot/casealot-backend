package kr.casealot.shop.global.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * https://www.baeldung.com/spring-response-status
 */
@ResponseStatus(code = HttpStatus.CONFLICT)
public class IncorrectException extends RuntimeException{
    public IncorrectException(String message) {
        super(message);
    }
}
