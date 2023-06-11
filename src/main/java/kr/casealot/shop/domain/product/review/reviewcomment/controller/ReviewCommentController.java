package kr.casealot.shop.domain.product.review.reviewcomment.controller;

import io.swagger.annotations.Api;
import kr.casealot.shop.domain.product.review.reviewcomment.dto.ReviewCommentReqDTO;
import kr.casealot.shop.domain.product.review.reviewcomment.dto.ReviewCommentResDTO;
import kr.casealot.shop.domain.product.review.reviewcomment.service.ReviewCommentService;
import kr.casealot.shop.global.common.APIResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@Slf4j
@RequiredArgsConstructor
@Api(tags = {"REVIEWCOMMENT API"}, description = "리뷰 댓글 관련 API")
@RequestMapping("/cal/v1/review/comment")
public class ReviewCommentController {
    private final ReviewCommentService reviewCommentService;

    @PostMapping("/{reviewSeq}")
    private APIResponse<ReviewCommentResDTO> createReview(@RequestBody ReviewCommentReqDTO reviewCommentReqDTO, @PathVariable long reviewSeq, Principal principal) {
        return reviewCommentService.createReviewComment(reviewCommentReqDTO, reviewSeq, principal);
    }

    //수정
    @PutMapping("/{reviewCommentId}")
    private APIResponse<ReviewCommentResDTO> createReview(@PathVariable Long reviewCommentId, @RequestBody ReviewCommentReqDTO reviewCommentReqDTO, Principal principal) {
        return reviewCommentService.fixReviewComment(reviewCommentId, reviewCommentReqDTO, principal);
    }

    //삭제
    @DeleteMapping("/{reviewCommentId}")
    private APIResponse<ReviewCommentResDTO> deleteReview(@PathVariable Long reviewCommentId, Principal principal) {
        return reviewCommentService.deleteReviewComment(reviewCommentId, principal);
    }
}
