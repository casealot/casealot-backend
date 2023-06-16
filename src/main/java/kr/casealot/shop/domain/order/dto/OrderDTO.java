package kr.casealot.shop.domain.order.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import kr.casealot.shop.domain.customer.entity.Customer;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

public class OrderDTO {

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request{
        private String orderNumber;
        private List<OrderProductDTO> orderProducts;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response{
        private Long id;
        private String orderNumber;
        private String customerId;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private LocalDateTime orderDt;
        private OrderStatus orderStatus;
        private int totalAmount;
        private List<OrderProductDTO> orderProducts;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderProductDTO {
        private Long productId;
        private int price;
        private int quantity;
    }
}
