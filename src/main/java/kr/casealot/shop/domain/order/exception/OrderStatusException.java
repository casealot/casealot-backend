package kr.casealot.shop.domain.order.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * https://www.baeldung.com/spring-response-status
 */
@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class OrderStatusException extends RuntimeException{
    static final String INVALID_STATUS = "올바르지 않은 상태값 입니다.";
    public OrderStatusException( ) {
        super(INVALID_STATUS);
    }
}
