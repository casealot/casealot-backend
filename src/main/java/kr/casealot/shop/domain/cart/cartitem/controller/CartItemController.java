package kr.casealot.shop.domain.cart.cartitem.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import kr.casealot.shop.domain.cart.cartitem.service.CartItemService;
import kr.casealot.shop.domain.cart.dto.CartGetDTO;
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
  @ApiOperation(value = "장바구니에 담겨있는 상품 개수 줄이기", notes = "장바구니에 이미 담긴 상품의 개수를 줄인다.")
  public APIResponse<CartGetDTO> reduceCartItemQuantity(Principal principal,
      @ApiParam(value = "카트에 들어있는 CartItemId 번호") @PathVariable Long cartItemId) {
    return cartItemService.reduceCartItemQuantity(principal, cartItemId);
  }

  //카트 상품 증가
  @PostMapping("/add/{cartItemId}")
  @ApiOperation(value = "장바구니에 담겨있는 상품 개수 늘이기", notes = "장바구니에 이미 담긴 상품의 개수를 늘린다.")
  public APIResponse<CartGetDTO> addCartItemQuantity(Principal principal,
      @ApiParam(value = "카트에 들어있는 CartItemId 번호") @PathVariable Long cartItemId) {
    return cartItemService.addCartItemQuantity(principal, cartItemId);
  }

  //단일 삭제
  @DeleteMapping("/{cartItemId}")
  @ApiOperation(value = "장바구니에 담겨있는 상품 품목 삭제", notes = "장바구니에 이미 담긴 상품을 단일 삭제처리한다.")
  public APIResponse<CartGetDTO> removeCartItem(Principal principal,
      @ApiParam(value = "카트에 들어있는 CartItemId 번호") @PathVariable Long cartItemId) {
    return cartItemService.removeCartItem(principal, cartItemId);
  }


}