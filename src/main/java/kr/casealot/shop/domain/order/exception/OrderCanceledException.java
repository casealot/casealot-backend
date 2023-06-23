package kr.casealot.shop.domain.order.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * https://www.baeldung.com/spring-response-status
 */
@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class OrderCanceledException extends RuntimeException{
    static final String ORDER_CANCELED= "취소된 주문입니다.";
    public OrderCanceledException() {
        super(ORDER_CANCELED);
    }
}
