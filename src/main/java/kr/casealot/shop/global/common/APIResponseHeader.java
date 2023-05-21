package kr.casealot.shop.global.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class APIResponseHeader {
    private int code;
    private String message;
}
