package kr.casealot.shop.domain.cart.controller;

import io.swagger.annotations.Api;
import kr.casealot.shop.domain.cart.dto.CartResDto;
import kr.casealot.shop.domain.cart.entity.Cart;
import kr.casealot.shop.domain.cart.service.CartService;
import kr.casealot.shop.global.common.APIResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@Slf4j
@RequiredArgsConstructor
@Api(tags = {"CART API"}, description = "유저 관련 API")
@RequestMapping("/cal/v1/cart")
public class CartController {
    private final CartService cartService;

    @PostMapping("/{customerId}/items")
    public APIResponse<CartResDto> addItemToCart(
            Principal principal,
            @RequestParam Long productId,
            @RequestParam int quantity
    ) {
        return cartService.addItemToCart(principal, productId, quantity);
    }

    @DeleteMapping("/clear")
    public APIResponse<Cart> clearCart(Principal principal) {
        return cartService.clearCart(principal);
    }
}
