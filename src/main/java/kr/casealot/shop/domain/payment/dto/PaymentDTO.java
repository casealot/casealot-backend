package kr.casealot.shop.domain.payment.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import kr.casealot.shop.domain.payment.entity.PaymentMethod;
import kr.casealot.shop.domain.payment.entity.PaymentStatus;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class PaymentDTO {
    private Long id;
    private Long orderId;
    private String customerId;
    private String receiptId;
    private String orderNumber;
    private PaymentMethod method;
    private BigDecimal amount;
    private PaymentStatus status;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime paidAt;
    private BigDecimal cancelledAmount;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime cancelledAt;
}
