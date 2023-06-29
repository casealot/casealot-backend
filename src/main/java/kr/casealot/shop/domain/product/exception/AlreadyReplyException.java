package kr.casealot.shop.domain.product.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * https://www.baeldung.com/spring-response-status
 */
@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class AlreadyReplyException extends RuntimeException{
    static final String ALREADY_REVIEW = "이미 상품에 대한 댓글을 작성하셨습니다.";
    public AlreadyReplyException( ) {
        super(ALREADY_REVIEW);
    }
}
