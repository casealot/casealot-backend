package kr.casealot.shop.domain.cart.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartResDto {
    private String productName;
    private int quantity;
}
