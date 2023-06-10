package kr.casealot.shop.global.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * https://www.baeldung.com/spring-response-status
 */
@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class NoRefreshTokenException extends RuntimeException{
    static final String NOT_FOUND_TOKEN = "리프레시 토큰값을 받지 못하였습니다.";
    public NoRefreshTokenException( ) {
        super(NOT_FOUND_TOKEN);
    }
}
