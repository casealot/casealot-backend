package kr.casealot.shop.domain.wishlist.dto;

import kr.casealot.shop.domain.product.entity.Product;
import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WishlistResDTO {
    private String customerId;
    private Long productId;
    private String productName;
}
