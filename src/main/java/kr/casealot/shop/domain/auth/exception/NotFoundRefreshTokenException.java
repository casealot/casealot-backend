package kr.casealot.shop.domain.auth.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * https://www.baeldung.com/spring-response-status
 */
@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class NotFoundRefreshTokenException extends RuntimeException{
    static final String NOT_FOUND_TOKEN = "리프레시 토큰값이 존재하지 않습니다.";
    public NotFoundRefreshTokenException( ) {
        super(NOT_FOUND_TOKEN);
    }
}
