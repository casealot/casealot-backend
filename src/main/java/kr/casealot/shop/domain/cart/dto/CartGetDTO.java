package kr.casealot.shop.domain.cart.dto;

import kr.casealot.shop.domain.product.dto.ProductCartDTO;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartGetDTO {

    private Long customerSeq;
    private String customerName;
    private Long cartId;
    private List<ProductCartDTO> products; // 각 상품의 정보를 담을 리스트

}
