package kr.casealot.shop.domain.payment.dto;

import lombok.Getter;

@Getter
public class CancelPaymentReq {
    private String receiptId;
    private String orderId;
}
