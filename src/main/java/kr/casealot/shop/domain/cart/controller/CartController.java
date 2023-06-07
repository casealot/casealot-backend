package kr.casealot.shop.domain.cart.controller;

import io.swagger.annotations.Api;
import kr.casealot.shop.domain.cart.entity.Cart;
import kr.casealot.shop.domain.cart.service.CartService;
import kr.casealot.shop.global.common.APIResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@Slf4j
@RequiredArgsConstructor
@Api(tags = {"CART API"}, description = "유저 관련 API")
@RequestMapping("/cal/v1/cart")
public class CartController {
    private final CartService cartService;

    @PostMapping("/{customerId}/items")
    public APIResponse<Cart> addItemToCart(
            Principal principal,
            @RequestParam Long productId,
            @RequestParam int quantity
    ) {
        return cartService.addItemToCart(principal, productId, quantity);

    }
}
