package kr.casealot.shop.global.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * https://www.baeldung.com/spring-response-status
 */
@ResponseStatus(code = HttpStatus.CONFLICT)
public class DuplicateIdException extends Exception{
    static final String DUPLICATE_ID = "중복되는 아이디가 이미 존재합니다";
    public DuplicateIdException() {
        super(DUPLICATE_ID);
    }
}
