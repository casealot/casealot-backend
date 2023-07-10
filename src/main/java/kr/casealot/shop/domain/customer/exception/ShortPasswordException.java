package kr.casealot.shop.domain.customer.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * https://www.baeldung.com/spring-response-status
 */
@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class ShortPasswordException extends RuntimeException{
    static final String SHORT_PASSWORD = "비밀번호는 8자 이상이여야 합니다.";
    public ShortPasswordException() {
        super(SHORT_PASSWORD);
    }
}
