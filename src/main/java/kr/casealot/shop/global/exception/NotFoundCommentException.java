package kr.casealot.shop.global.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * https://www.baeldung.com/spring-response-status
 */
@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class NotFoundCommentException extends RuntimeException{
    static final String NOT_FOUND_COMMENT = "존재하지 않는 댓글입니다.";
    public NotFoundCommentException( ) {
        super(NOT_FOUND_COMMENT);
    }
}
