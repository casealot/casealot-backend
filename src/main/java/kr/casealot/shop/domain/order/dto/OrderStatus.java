package kr.casealot.shop.domain.order.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@AllArgsConstructor
public enum OrderStatus {
    ORDER("접수"),
    CANCEL("취소"),
    CHANGE("교환"),
    COMPLETE("완료");
    private final String displayName;
}
