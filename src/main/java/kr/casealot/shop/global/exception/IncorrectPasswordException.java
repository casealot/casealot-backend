package kr.casealot.shop.global.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * https://www.baeldung.com/spring-response-status
 */
@ResponseStatus(code = HttpStatus.CONFLICT)
public class IncorrectPasswordException extends RuntimeException{
    static final String INCORRECT_PASSWORD = "비밀번호가 일치하지 않습니다.";
    public IncorrectPasswordException() {
        super(INCORRECT_PASSWORD);
    }
}
