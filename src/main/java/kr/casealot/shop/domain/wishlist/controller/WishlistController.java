package kr.casealot.shop.domain.wishlist.controller;

import io.swagger.annotations.Api;
import kr.casealot.shop.domain.wishlist.dto.WishlistResDTO;
import kr.casealot.shop.domain.wishlist.service.WishlistService;
import kr.casealot.shop.domain.wishlist.wishlistItem.repository.WishlistItemRepository;
import kr.casealot.shop.global.common.APIResponse;
import kr.casealot.shop.global.exception.DuplicateProductException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

@RestController
@RequiredArgsConstructor
@Api(tags = {"WISHLIST API"}, description = "WISHLIST 관련 API")
@RequestMapping("/cal/v1/wishlist")
public class WishlistController {

    private final WishlistService wishlistService;
    private final WishlistItemRepository wishlistItemRepository;

    @PostMapping("/{productId}")
    public APIResponse<WishlistResDTO> addProductToWishlist(@PathVariable Long productId,
                                                            HttpServletRequest request,
                                                            Principal principal) throws DuplicateProductException {

        return wishlistService.addProductToWishlist(productId, request, principal);
    }


    @GetMapping
    public APIResponse<WishlistResDTO> getWishlist(Principal principal){

        return wishlistService.getWishlistItems(principal);
    }

    @DeleteMapping("/{productId}")
    public APIResponse<WishlistResDTO> deleteProductToWishlist(@PathVariable Long productId,
                                                               HttpServletRequest request,
                                                               Principal principal) {
        return wishlistService.deleteProductToWishlist(productId, request, principal);
    }

    // 전체삭제
    @DeleteMapping
    public APIResponse<WishlistResDTO> deleteWishlist(Principal principal){

        return wishlistService.deleteWishlist(principal);
    }

    @GetMapping("/{productId}")
    public ResponseEntity getCustomerCountByProductId(@PathVariable Long productId, Principal principal) {
        int customerCount = wishlistService.getCustomerCountForProduct(productId, principal);
        return ResponseEntity.ok(customerCount);
    }
}
