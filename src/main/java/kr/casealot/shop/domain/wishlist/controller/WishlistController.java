package kr.casealot.shop.domain.wishlist.controller;

import io.swagger.annotations.Api;
import kr.casealot.shop.domain.wishlist.dto.WishlistReqDTO;
import kr.casealot.shop.domain.wishlist.dto.WishlistResDTO;
import kr.casealot.shop.domain.wishlist.service.WishlistService;
import kr.casealot.shop.global.common.APIResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

@RestController
@RequiredArgsConstructor
@Api(tags = {"WISHLIST API"}, description = "WISHLIST 관련 API")
@RequestMapping("/cal/v1/wishlist")
public class WishlistController {

    private final WishlistService wishlistService;

    @PostMapping("/add")
    public APIResponse<WishlistResDTO> addProductToWishlist(@RequestBody WishlistReqDTO wishlistReqDTO,
                                   HttpServletRequest request,
                                   Principal principal){

        return wishlistService.addProductToWishlist(wishlistReqDTO, request, principal);
    }

    @DeleteMapping("/delete")
    public APIResponse<WishlistResDTO> deleteProductToWishlist(@RequestBody WishlistReqDTO wishlistReqDTO,
                                                   HttpServletRequest request,
                                                   Principal principal){

        return wishlistService.deleteProductToWishlist(wishlistReqDTO, request, principal);
    }
}