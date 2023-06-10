package kr.casealot.shop.domain.order.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@AllArgsConstructor
public enum OrderStatus {
    ORDER("주문"),
    CANCEL("주문취소");
    private final String displayName;
}
