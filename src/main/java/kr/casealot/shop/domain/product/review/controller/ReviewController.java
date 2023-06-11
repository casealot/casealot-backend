package kr.casealot.shop.domain.product.review.controller;

import io.swagger.annotations.Api;
import kr.casealot.shop.domain.product.review.dto.ReviewReqDTO;
import kr.casealot.shop.domain.product.review.dto.ReviewResDTO;
import kr.casealot.shop.domain.product.review.service.ReviewService;
import kr.casealot.shop.global.common.APIResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

// /cal/v1/product/{id}

@RestController
@Slf4j
@RequiredArgsConstructor
@Api(tags = {"REVIEW API"}, description = "리뷰 관련 API")
@RequestMapping("/cal/v1/review")
public class ReviewController {
    private final ReviewService reviewService;

    //생성
    @PostMapping("/{productId}/create")
    private APIResponse<ReviewResDTO> createReview(@RequestBody ReviewReqDTO reviewReqDTO, @PathVariable Long productId, Principal principal) {
        return reviewService.createReview(reviewReqDTO, productId, principal);
    }

    //수정
    @PutMapping("/{reviewId}")
    private APIResponse<ReviewResDTO> createReview(@PathVariable Long reviewId, @RequestBody ReviewReqDTO reviewReqDTO, Principal principal) {
        return reviewService.fixReview(reviewId, reviewReqDTO, principal);
    }

    //삭제
    @DeleteMapping("/{reviewId}")
    private APIResponse<ReviewResDTO> deleteReview(@PathVariable Long reviewId, Principal principal) {
        return reviewService.deleteReview(reviewId, principal);
    }

    @GetMapping("/{reviewId}")
    private APIResponse<ReviewResDTO> viewReview(@PathVariable Long reviewId) {
        return reviewService.getReview(reviewId);
    }
}