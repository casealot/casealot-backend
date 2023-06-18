package kr.casealot.shop.domain.cart.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
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
  @ApiOperation(value = "장바구니에 상품 단일 추가", notes = "상품 하나를 장바구니에 담는다.")
  public APIResponse<CartGetDTO> addItemToCart(Principal principal,
      @ApiParam(value = "장바구니에 추가할 ProductID") @PathVariable Long productId) {
    return cartService.addItemToCart(principal, productId);
  }

  @PostMapping("/items/{productId}/{quantity}")
  @ApiOperation(value = "장바구니에 상품을 원하는 만큼 추가", notes = "상품 하나를 원하는 개수만큼 장바구니에 담는다.")
  public APIResponse<CartGetDTO> addManyItemToCart(Principal principal,
      @ApiParam(value = "장바구니에 추가할 ProductID") @PathVariable Long productId,
      @ApiParam(value = "장바구니에 추가할 상품의 양") @PathVariable int quantity) {
    return cartService.addManyItemToCart(principal, productId, quantity);
  }

  @DeleteMapping("/clear")
  @ApiOperation(value = "장바구니 초기화", notes = "장바구니를 초기화 한다.")
  public APIResponse<String> clearCart(Principal principal) {
    return cartService.clearCart(principal);
  }

  @GetMapping
  @ApiOperation(value = "장바구니 정보 가져오기", notes = "장바구니에 담긴 상품의 정보를 가져온다.")
  public APIResponse<CartGetDTO> getCart(Principal principal) {
    return cartService.getCart(principal);
  }
}
