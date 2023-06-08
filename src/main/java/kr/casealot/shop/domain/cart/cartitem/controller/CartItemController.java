package kr.casealot.shop.domain.cart.cartitem.controller;

import io.swagger.annotations.Api;
import kr.casealot.shop.domain.cart.cartitem.service.CartItemService;
import kr.casealot.shop.domain.cart.dto.CartResDTO;
import kr.casealot.shop.global.common.APIResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/cal/v1/cart")
@RequiredArgsConstructor
@Api(tags = {"CART ITEM API"}, description = "카트 아이템 관련 API")
public class CartItemController {
    private final CartItemService cartItemService;

    @PostMapping("/reduce/{cartItemId}")
    public APIResponse<List<CartResDTO>> reduceCartItemQuantity(Principal principal,
                                                                @PathVariable Long cartItemId) {
        return cartItemService.reduceCartItemQuantity(principal, cartItemId);
    }

    //카트 상품 증가
    @PostMapping("/add/{cartItemId}")
    public APIResponse<List<CartResDTO>> addCartItemQuantity(Principal principal,
                                                             @PathVariable Long cartItemId) {
        return cartItemService.addCartItemQuantity(principal, cartItemId);
    }

    //단일 삭제
    @DeleteMapping("/{cartItemId}")
    public APIResponse<List<CartResDTO>> removeCartItem(Principal principal,
                                                        @PathVariable Long cartItemId) {
        return cartItemService.removeCartItem(principal, cartItemId);
    }


}