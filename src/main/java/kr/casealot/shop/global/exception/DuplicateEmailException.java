package kr.casealot.shop.global.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * https://www.baeldung.com/spring-response-status
 */
@ResponseStatus(code = HttpStatus.CONFLICT)
public class DuplicateEmailException extends Exception{
    static final String DUPLICATE_EMAIL = "중복되는 이메일이 이미 존재합니다";
    public DuplicateEmailException() {
        super(DUPLICATE_EMAIL);
    }
}
