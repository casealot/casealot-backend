package kr.casealot.shop.domain.wishlist.controller;

import io.swagger.annotations.Api;
import kr.casealot.shop.domain.wishlist.dto.WishlistReqDTO;
import kr.casealot.shop.domain.wishlist.service.WishlistService;
import kr.casealot.shop.global.common.APIResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
@Api(tags = {"WISHLIST API"}, description = "WISHLIST 관련 API")
@RequestMapping("/cal/v1/wishlist")
public class WishlistController {

    private final WishlistService wishlistService;

    @PostMapping("/addProduct")
    public void addWishlist(@RequestBody WishlistReqDTO wishlistReqDTO,
                                                   HttpServletRequest request){

//        return wishlistService.addWishlist(wishlistReqDTO, request);
    }
}
