package kr.casealot.shop.domain.order.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@AllArgsConstructor
public enum OrderStatus {
    ORDER("접수"),
    IN_PROGRESS("처리중"),
    CANCEL("취소"),
    COMPLETE("완료");
    private final String displayName;
}
