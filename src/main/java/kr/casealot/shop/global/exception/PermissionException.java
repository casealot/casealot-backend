package kr.casealot.shop.global.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * https://www.baeldung.com/spring-response-status
 */
@ResponseStatus(code = HttpStatus.UNAUTHORIZED)
public class PermissionException extends RuntimeException{
    public PermissionException(String message) {
        super(message);
    }
}
