package kr.casealot.shop.domain.wishlist.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import kr.casealot.shop.domain.wishlist.dto.WishlistResDTO;
import kr.casealot.shop.domain.wishlist.service.WishlistService;
import kr.casealot.shop.global.common.APIResponse;
import kr.casealot.shop.global.exception.DuplicateProductException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@Api(tags = {"WISHLIST API"}, description = "WISHLIST 관련 API")
@RequestMapping("/cal/v1/wishlist")
public class WishlistController {

  private final WishlistService wishlistService;

  @PostMapping("/{productId}")
  @ApiOperation(value = "위시리스트 상품 등록", notes = "위시리스트에 상품을 등록한다.")
  public APIResponse<WishlistResDTO> addProductToWishlist(
      @ApiParam(value = "상품 ID") @PathVariable Long productId,
      Principal principal) throws DuplicateProductException {

    return wishlistService.addProductToWishlist(productId, principal);
  }


  @GetMapping
  @ApiOperation(value = "위시리스트 조회", notes = "위시리스트를 조회한다.")
  public APIResponse<WishlistResDTO> getWishlist(Principal principal) {

    return wishlistService.getWishlistItems(principal);
  }

  @DeleteMapping("/{productId}")
  @ApiOperation(value = "위시리스트 상품 삭제", notes = "위시리스트에 등록된 상품을 삭제한다.")
  public APIResponse<WishlistResDTO> deleteProductToWishlist(
      @ApiParam(value = "상품 ID") @PathVariable Long productId,
      Principal principal) {
    return wishlistService.deleteProductToWishlist(productId, principal);
  }

  // 전체삭제
  @DeleteMapping
  @ApiOperation(value = "위시리스트 전체 삭제", notes = "위시리스트 목록을 전체 삭제한다.")
  public APIResponse<WishlistResDTO> deleteWishlist(Principal principal) {

    return wishlistService.deleteWishlist(principal);
  }

  @GetMapping("/{productId}")
  @ApiOperation(value = "위시리스트 등록 확인", notes = "상품 상세보기 페이지에서 위시리스트에 등록된 상품인지 확인한다.")
  public ResponseEntity getCustomerCountByProductId(
      @ApiParam(value = "상품 ID") @PathVariable Long productId) {
    int customerCount = wishlistService.getCustomerCountForProduct(productId);
    return ResponseEntity.ok(customerCount);
  }
}
