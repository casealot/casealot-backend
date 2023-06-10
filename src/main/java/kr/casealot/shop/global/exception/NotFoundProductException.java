package kr.casealot.shop.global.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * https://www.baeldung.com/spring-response-status
 */
@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class NotFoundProductException extends RuntimeException{
    static final String NOT_FOUND_PRODUCT = "존재하지 않는 상품입니다.";
    public NotFoundProductException( ) {
        super(NOT_FOUND_PRODUCT);
    }
}
