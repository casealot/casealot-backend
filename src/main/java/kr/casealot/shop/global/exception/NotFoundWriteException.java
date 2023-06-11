package kr.casealot.shop.global.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * https://www.baeldung.com/spring-response-status
 */
@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class NotFoundWriteException extends RuntimeException{
    static final String NOT_FOUND_WRITE = "존재하지 않는 게시글입니다.";
    public NotFoundWriteException( ) {
        super(NOT_FOUND_WRITE);
    }
}
