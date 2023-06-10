package kr.casealot.shop.domain.order.entity;

import kr.casealot.shop.domain.customer.entity.Customer;
import kr.casealot.shop.domain.order.dto.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "order")
public class Order{
    @Id
    @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer; //주문 회원

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderProduct> orderItems = new ArrayList<>();

//    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    @JoinColumn(name = "delivery_id")
//    private Delivery delivery; //배송정보

    @Column(name = "order_dt")
    private LocalDateTime orderDt; //주문시간

    @Column(name = "order_status")
    @Enumerated(EnumType.STRING)
    private OrderStatus status; //주문상태 [ORDER, CANCEL]
    //==생성 메서드==//
//    public static Order createOrder(Customer customer, Delivery delivery, OrderItem... orderItems) {
//        Order order = new Order();
//        order.setCustomer(member);
//        order.setDelivery(delivery);
//        for (OrderItem orderItem : orderItems) {
//            order.addOrderItem(orderItem);
//        }
//        order.setStatus(OrderStatus.ORDER);
//        order.setOrderDate(LocalDateTime.now());
//        return order;
//    }
//    //==비즈니스 로직==//
//    /** 주문 취소 */
//    public void cancel() {
//        if (delivery.getStatus() == DeliveryStatus.COMP) {
//            throw new IllegalStateException("이미 배송완료된 상품은 취소가 불가능합니다.");
//        }
//
//        this.setStatus(OrderStatus.CANCEL);
//        for (OrderItem orderItem : orderItems) {
//            orderItem.cancel();
//        }
//    }
//
//    //==조회 로직==//
//    /** 전체 주문 가격 조회 */
//    public int getTotalPrice() {
//        int totalPrice = 0;
//        for (OrderItem orderItem : orderItems) {
//            totalPrice += orderItem.getTotalPrice();
//        }
//        return totalPrice;
//    }
}
