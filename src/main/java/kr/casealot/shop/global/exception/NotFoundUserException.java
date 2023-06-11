package kr.casealot.shop.global.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * https://www.baeldung.com/spring-response-status
 */
@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class NotFoundUserException extends RuntimeException{
    static final String NOT_FOUND_USER = "존재하지 않는 회원에 대한 요청입니다.";
    public NotFoundUserException( ) {
        super(NOT_FOUND_USER);
    }
}
