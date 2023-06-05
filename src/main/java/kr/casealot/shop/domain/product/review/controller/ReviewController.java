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

// /cal/v1/product/{id}

@RestController
@Slf4j
@RequiredArgsConstructor
@Api(tags = {"REVIEW API"}, description = "리뷰 관련 API")
@RequestMapping("/cal/v1/product/{id}/review")
public class ReviewController {
    private final ReviewService reviewService;

    //생성
    @PostMapping("/create")
    private APIResponse createReview(@RequestBody ReviewReqDTO reviewReqDTO, HttpServletRequest request, @PathVariable Long id) {
        return reviewService.createReview(reviewReqDTO, request, id);
    }

    //수정
    @PutMapping("/fix/{reviewId}")
    private APIResponse createReview(@PathVariable Long reviewId, @RequestBody ReviewReqDTO reviewReqDTO, HttpServletRequest request) {
        return reviewService.fixReview(reviewId,reviewReqDTO, request);
    }

    //삭제
    @DeleteMapping("/delete/{reviewId}")
    private APIResponse deleteReview(@PathVariable Long reviewId, HttpServletRequest request) {
        return reviewService.deleteReview(reviewId, request);
    }

    @GetMapping("/view/{reviewId}")
    private APIResponse viewReview(@PathVariable Long reviewId) throws ChangeSetPersister.NotFoundException {
        return reviewService.getReview(reviewId);
    }
}