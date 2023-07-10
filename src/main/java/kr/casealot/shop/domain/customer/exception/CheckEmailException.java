package kr.casealot.shop.domain.customer.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * https://www.baeldung.com/spring-response-status
 */
@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class CheckEmailException extends RuntimeException{
    static final String INCORRECT_EMAIL = "이메일을 확인하세요.";
    public CheckEmailException() {
        super(INCORRECT_EMAIL);
    }
}
