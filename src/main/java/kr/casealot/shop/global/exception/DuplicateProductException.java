package kr.casealot.shop.global.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * https://www.baeldung.com/spring-response-status
 */
@ResponseStatus(code = HttpStatus.CONFLICT)
public class DuplicateProductException extends Exception{
    static final String DUPLICATE_PRODUCT = "중복되는 상품명이 이미 존재합니다";
    public DuplicateProductException() {
        super(DUPLICATE_PRODUCT);
    }
}
