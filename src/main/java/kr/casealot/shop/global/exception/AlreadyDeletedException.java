package kr.casealot.shop.global.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * https://www.baeldung.com/spring-response-status
 */
@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class AlreadyDeletedException extends RuntimeException{
    static final String ALREADY_DELETED = "이미 삭제되었거나 존재하지않습니다.";
    public AlreadyDeletedException( ) {
        super(ALREADY_DELETED);
    }
}
