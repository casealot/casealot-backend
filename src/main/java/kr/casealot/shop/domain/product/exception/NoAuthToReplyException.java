package kr.casealot.shop.domain.product.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * https://www.baeldung.com/spring-response-status
 */
@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class NoAuthToReplyException extends RuntimeException{
    static final String CANT_REVIEW = "상품을 구매하지 않아 댓글을 달 수 없습니다.";
    public NoAuthToReplyException( ) {
        super(CANT_REVIEW);
    }
}
