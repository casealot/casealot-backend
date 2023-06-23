package kr.casealot.shop.domain.payment.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
public class ConnectException extends RuntimeException {
    static final String CONNECTION_ERROR = "서버와의 연결에 문제가 발생했습니다.";

    public ConnectException() {
        super(CONNECTION_ERROR);
    }
}