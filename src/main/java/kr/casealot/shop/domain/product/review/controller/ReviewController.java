package kr.casealot.shop.domain.product.review.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import kr.casealot.shop.domain.product.review.dto.ReviewReqDTO;
import kr.casealot.shop.domain.product.review.dto.ReviewResDTO;
import kr.casealot.shop.domain.product.review.service.ReviewService;
import kr.casealot.shop.global.common.APIResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;


@RestController
@Slf4j
@RequiredArgsConstructor
@Api(tags = {"REVIEW API"}, description = "리뷰 관련 API")
@RequestMapping("/cal/v1/review")
public class ReviewController {

  private final ReviewService reviewService;

  //생성
  @PostMapping("/{productId}")
  @ApiOperation(value = "리뷰 작성", notes = "상품을 구매한 사용자가 리뷰를 작성한다.")
  private APIResponse<ReviewResDTO> createReview(
      @ApiParam(value = "리뷰 등록/수정 DTO") @RequestBody ReviewReqDTO reviewReqDTO,
      @ApiParam(value = "상품 ID") @PathVariable Long productId,
      Principal principal) {
    return reviewService.createReview(reviewReqDTO, productId, principal);
  }

  //수정
  @PutMapping("/{reviewId}")
  @ApiOperation(value = "리뷰 수정", notes = "리뷰를 작성한 사용자가 리뷰를 수정한다.")
  private APIResponse<ReviewResDTO> createReview(
      @ApiParam(value = "리뷰 ID") @PathVariable Long reviewId,
      @ApiParam(value = "리뷰 등록/수정 DTO") @RequestBody ReviewReqDTO reviewReqDTO,
      Principal principal) {
    return reviewService.fixReview(reviewId, reviewReqDTO, principal);
  }

  //삭제
  @DeleteMapping("/{reviewId}")
  @ApiOperation(value = "리뷰 삭제", notes = "리뷰를 작성한 사용자가 리뷰를 삭제한다.")
  private APIResponse<ReviewResDTO> deleteReview(
      @ApiParam(value = "리뷰 ID") @PathVariable Long reviewId,
      Principal principal) {
    return reviewService.deleteReview(reviewId, principal);
  }

  @GetMapping("/{reviewId}")
  @ApiOperation(value = "특정 리뷰 조회", notes = "전체 사용자가 리뷰를 조회한다. (사용 X 테스트용)")
  private APIResponse<ReviewResDTO> viewReview(
      @ApiParam(value = "리뷰 ID") @PathVariable Long reviewId) {
    return reviewService.getReview(reviewId);
  }
}