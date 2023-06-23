package kr.casealot.shop.domain.order.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * https://www.baeldung.com/spring-response-status
 */
@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class OrderAlreadyCompleteException extends RuntimeException{
    static final String ALREADY_COMPLETE= "주문이 이미 완료되었습니다. ";
    public OrderAlreadyCompleteException() {
        super(ALREADY_COMPLETE);
    }
}
