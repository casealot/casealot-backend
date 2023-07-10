package kr.casealot.shop.domain.customer.exception;

import com.amazonaws.services.kms.model.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * https://www.baeldung.com/spring-response-status
 */
@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class CheckNameException extends RuntimeException {
    static final String INCORRECT_NAME = "이름을 확인하세요.";
    public CheckNameException() {
        super(INCORRECT_NAME);
    }
}
