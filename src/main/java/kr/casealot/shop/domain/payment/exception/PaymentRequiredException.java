package kr.casealot.shop.domain.payment.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.PAYMENT_REQUIRED)
public class PaymentRequiredException extends RuntimeException {
    static final String PAYMENT_REQUIRED = "결제가 완료되지 않았습니다.";

    public PaymentRequiredException() {
        super(PAYMENT_REQUIRED);
    }
}