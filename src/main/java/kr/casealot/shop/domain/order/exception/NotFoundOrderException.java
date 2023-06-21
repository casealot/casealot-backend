package kr.casealot.shop.domain.order.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * https://www.baeldung.com/spring-response-status
 */
@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class NotFoundOrderException extends RuntimeException{
    static final String NOT_FOUND_ORDER = "주문내역이 존재하지 않습니다.";
    public NotFoundOrderException( ) {
        super(NOT_FOUND_ORDER);
    }
}
