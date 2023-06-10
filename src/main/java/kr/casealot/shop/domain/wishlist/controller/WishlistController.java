package kr.casealot.shop.domain.wishlist.controller;

import io.swagger.annotations.Api;
import kr.casealot.shop.domain.wishlist.service.WishlistService;
import kr.casealot.shop.domain.wishlist.wishlistItem.dto.WishlistItemResDTO;
import kr.casealot.shop.global.common.APIResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Api(tags = {"WISHLIST API"}, description = "WISHLIST 관련 API")
@RequestMapping("/cal/v1/wishlist")
public class WishlistController {

    private final WishlistService wishlistService;

    @PostMapping("/add/{productId}")
    public APIResponse<WishlistItemResDTO> addProductToWishlist(@PathVariable Long productId,
                                                                HttpServletRequest request,
                                                                Principal principal) {
        return wishlistService.addProductToWishlist(productId, request, principal);
    }

    @DeleteMapping("/delete/{productId}")
    public APIResponse<WishlistItemResDTO> deleteProductToWishlist(@PathVariable Long productId,
                                                                   HttpServletRequest request,
                                                                   Principal principal) {
        return wishlistService.deleteProductToWishlist(productId, request, principal);
    }


    // 전체삭제
    @DeleteMapping("/delete")
    public APIResponse<List<WishlistItemResDTO>> deleteWishlist(Principal principal){

        return wishlistService.deleteWishlist(principal);
    }

    @GetMapping
    public APIResponse<List<WishlistItemResDTO>> getWishlist(Principal principal){

        return wishlistService.getWishlistItems(principal);
    }
}
