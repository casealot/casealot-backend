package kr.casealot.shop.domain.wishlist.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WishlistResDTO {
    private List<Long> prductIdList;
}
