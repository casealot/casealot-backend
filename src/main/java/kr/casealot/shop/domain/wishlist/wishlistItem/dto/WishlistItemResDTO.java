package kr.casealot.shop.domain.wishlist.wishlistItem.dto;

import kr.casealot.shop.domain.file.entity.UploadFile;
import kr.casealot.shop.domain.product.entity.Product;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WishlistItemResDTO {
    private Long id;
    private String name;
    private int price;
    private UploadFile thumbnail;
    private String content;
    private String color;
    private String season;
    private String type;
}
