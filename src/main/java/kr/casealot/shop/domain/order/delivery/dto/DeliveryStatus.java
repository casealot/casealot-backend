package kr.casealot.shop.domain.order.delivery.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DeliveryStatus {
  CANCELED("주문 취소"),
  READY("배송 준비중"),
  DELIVERING("배송중"),
  COMPLETE("배송 완료");
  private final String displayName;
}
