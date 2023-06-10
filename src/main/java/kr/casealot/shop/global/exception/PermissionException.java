package kr.casealot.shop.global.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * https://www.baeldung.com/spring-response-status
 */
@ResponseStatus(code = HttpStatus.UNAUTHORIZED)
public class PermissionException extends RuntimeException{
    static final String PERMISSION_DENIED = "올바르지 않은 권한입니다.";
    public PermissionException() {
        super(PERMISSION_DENIED);
    }
}
