package kr.casealot.shop.domain.wishlist.wishlistItem.controller;

import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Api(tags = {"WISHLIST_ITEM API"}, description = "WISHLIST_ITEM 관련 API")
@RequestMapping("/cal/v1/wishlist-item")
public class WishlistItemController {
}
