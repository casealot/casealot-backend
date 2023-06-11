package kr.casealot.shop.domain.wishlist.dto;

import kr.casealot.shop.domain.wishlist.wishlistItem.dto.WishlistItemDTO;
import lombok.*;

import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WishlistResDTO {
    private String customerId;
    private Long wishlistId;
    private List<WishlistItemDTO> productList;
}

