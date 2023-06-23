package kr.casealot.shop.domain.payment.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
public class PaymentFailedException extends RuntimeException {
    static final String PAYMENT_FAILED = "결제에 실패했습니다.";

    public PaymentFailedException() {
        super(PAYMENT_FAILED);
    }
}
