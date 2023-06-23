package kr.casealot.shop.domain.payment.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class PaymentCanceledException extends RuntimeException {
    static final String PAYMENT_CANCELED = "결제가 취소되었습니다.";

    public PaymentCanceledException() {
        super(PAYMENT_CANCELED);
    }
}
