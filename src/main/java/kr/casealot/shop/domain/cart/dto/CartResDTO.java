package kr.casealot.shop.domain.cart.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartResDTO {
    private Long cartId;
//    private Long productId;
    private String productName;
    private int quantity;
}
