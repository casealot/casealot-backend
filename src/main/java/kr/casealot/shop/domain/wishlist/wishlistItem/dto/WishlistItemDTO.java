package kr.casealot.shop.domain.wishlist.wishlistItem.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WishlistItemDTO {
    private Long id;
    private String name;
    private int price;
    private String thumbnail;
    private String content;
    private String color;
    private String season;
    private String type;
}
