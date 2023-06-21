package kr.casealot.shop.domain.payment.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * https://www.baeldung.com/spring-response-status
 */
@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class NotFoundPaymentException extends RuntimeException{
    static final String NOT_FOUND_ORDER = "결제 정보가 존재하지 않습니다.";
    public NotFoundPaymentException( ) {
        super(NOT_FOUND_ORDER);
    }
}
