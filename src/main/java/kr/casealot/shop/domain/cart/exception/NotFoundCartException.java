package kr.casealot.shop.domain.cart.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class NotFoundCartException extends RuntimeException {

  static final String NOT_FOUND_CART = "존재하지 않는 장바구니입니다.";

  public NotFoundCartException() {
    super(NOT_FOUND_CART);
  }
}