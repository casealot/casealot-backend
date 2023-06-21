package kr.casealot.shop.domain.order.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * https://www.baeldung.com/spring-response-status
 */
@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class OrderCancelException extends RuntimeException{
    static final String CANT_CANCEL_ORDER= "주문을 취소할 수 없습니다.";
    public OrderCancelException() {
        super(CANT_CANCEL_ORDER);
    }
}
