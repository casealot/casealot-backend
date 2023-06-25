package kr.casealot.shop.domain.payment.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import kr.casealot.shop.domain.customer.entity.Customer;
import lombok.*;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
@Builder
@Entity
@EntityListeners(AuditingEntityListener.class)
@Getter
@NoArgsConstructor
@Setter
@Table(name = "payments", indexes = @Index(name = "index_payments_order_id", columnList = "orderId"))
@ToString
public class Payment {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    private Long oId;

    @JoinColumn(name = "CUSTOMER_SEQ")
    @ManyToOne
    private Customer customer; // 구매자

    @Column(nullable = false, unique = true)
    private String receiptId; // PG 사에서 생성한 주문 번호

    @Column(nullable = false, unique = true)
    private String orderId; // 우리가 생성한 주문 번호

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PaymentMethod method; // 결제 수단

    @Column(nullable = false)
    private BigDecimal amount; // 결제 금액

    @Builder.Default
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PaymentStatus status = PaymentStatus.READY; // 상태

    @CreatedDate
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createAt; // 결제 요청 일시

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime paidAt; // 결제 완료 일시

    @Builder.Default
    private BigDecimal cancelledAmount = BigDecimal.ZERO; // 취소된 금액

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime cancelledAt; // 결제 취소 일시
}
