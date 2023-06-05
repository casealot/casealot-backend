package kr.casealot.shop.domain.product.review.reviewcomment.controller;

import io.swagger.annotations.Api;
import kr.casealot.shop.domain.product.review.reviewcomment.dto.ReviewCommentReqDTO;
import kr.casealot.shop.domain.product.review.reviewcomment.service.ReviewCommentService;
import kr.casealot.shop.global.common.APIResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@Slf4j
@RequiredArgsConstructor
@Api(tags = {"REVIEWCOMMENT API"}, description = "리뷰 댓글 관련 API")
@RequestMapping("/cal/v1/review/{reviewSeq}/comment")
public class ReviewCommentController {
    private final ReviewCommentService reviewCommentService;

    @PostMapping("/create")
    private APIResponse createReview(@RequestBody ReviewCommentReqDTO reviewCommentReqDTO, HttpServletRequest request, @PathVariable long reviewSeq) {
        return reviewCommentService.createReviewComment(reviewCommentReqDTO, reviewSeq, request);
    }

    //수정
    @PutMapping("/fix/{reviewCommentId}")
    private APIResponse createReview(@PathVariable Long reviewCommentId, @RequestBody ReviewCommentReqDTO reviewCommentReqDTO, HttpServletRequest request) {
        return reviewCommentService.fixReviewComment(reviewCommentId, reviewCommentReqDTO, request);
    }

    //삭제
    @DeleteMapping("/delete/{reviewCommentId}")
    private APIResponse deleteReview(@PathVariable Long reviewCommentId, HttpServletRequest request) {
        return reviewCommentService.deleteReviewComment(reviewCommentId, request);
    }
}
