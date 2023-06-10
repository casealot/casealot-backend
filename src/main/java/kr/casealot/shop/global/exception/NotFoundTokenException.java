package kr.casealot.shop.global.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * https://www.baeldung.com/spring-response-status
 */
@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class NotFoundTokenException extends RuntimeException{
    static final String NOT_FOUND_TOKEN = "리프레시 토큰값이 존재하지 않습니다.";
    public NotFoundTokenException( ) {
        super(NOT_FOUND_TOKEN);
    }
}
