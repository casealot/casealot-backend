package kr.casealot.shop.global.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * https://www.baeldung.com/spring-response-status
 */
@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class NoReviewException extends RuntimeException{
    static final String NOT_FOUND_REVIEW = "존재하지 않는 댓글에 대한 요청입니다.";
    public NoReviewException( ) {
        super(NOT_FOUND_REVIEW);
    }
}
