package kr.casealot.shop.global.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT)
public class DuplicateException extends Exception{
    public DuplicateException(String message) {
        super(message);
    }
}
