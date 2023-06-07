package kr.casealot.shop.domain.cart.cartitem.controller;

import kr.casealot.shop.domain.cart.cartitem.service.CartItemService;
import kr.casealot.shop.domain.cart.dto.CartResDto;
import kr.casealot.shop.domain.cart.entity.Cart;
import kr.casealot.shop.domain.cart.service.CartService;
import kr.casealot.shop.global.common.APIResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/cal/v1/cart")
@RequiredArgsConstructor
public class CartItemController {
    private final CartItemService cartItemService;

    @PostMapping("/{cartItemId}/reduce-quantity")
    public APIResponse<List<CartResDto>> reduceCartItemQuantity(Principal principal,
                                                                @PathVariable Long cartItemId,
                                                                @RequestParam("quantity") int quantity) {
        return cartItemService.reduceCartItemQuantity(principal, cartItemId, quantity);
    }

    @DeleteMapping("/{cartItemId}")
    public APIResponse<List<CartResDto>> removeCartItem(Principal principal,
                                                        @PathVariable Long cartItemId) {
        return cartItemService.removeCartItem(principal, cartItemId);
    }


}