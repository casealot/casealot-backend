package kr.casealot.shop.domain.cart.controller;

import io.swagger.annotations.Api;
import kr.casealot.shop.domain.cart.dto.CartGetDTO;
import kr.casealot.shop.domain.cart.dto.CartResDTO;
import kr.casealot.shop.domain.cart.service.CartService;
import kr.casealot.shop.global.common.APIResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@Slf4j
@RequiredArgsConstructor
@Api(tags = {"CART API"}, description = "카트 관련 API")
@RequestMapping("/cal/v1/cart")
public class CartController {
    private final CartService cartService;

    @PostMapping("/items/{productId}")
    public APIResponse<CartResDTO> addItemToCart(
            Principal principal,
            @PathVariable Long productId
    ) {
        return cartService.addItemToCart(principal, productId);
    }

    @DeleteMapping("/clear")
    public APIResponse<String> clearCart(Principal principal) {
        return cartService.clearCart(principal);
    }

    @GetMapping
    public APIResponse<CartGetDTO> getCart(Principal principal) {
        return cartService.getCart(principal);
    }
}
