package kr.casealot.shop.domain.product.review.reviewcomment.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
    @ApiOperation(value = "리뷰 댓글 작성", notes = "리뷰에 대한 댓글을 작성한다.")
    private APIResponse<ReviewCommentResDTO> createReview(@RequestBody ReviewCommentReqDTO reviewCommentReqDTO, @PathVariable long reviewSeq, Principal principal) {
        return reviewCommentService.createReviewComment(reviewCommentReqDTO, reviewSeq, principal);
    }

    //수정
    @PutMapping("/{reviewCommentId}")
    @ApiOperation(value = "리뷰 댓글 수정", notes = "리뷰에 대한 댓글을 작성한 사용자가 자신의 대댓글을 수정한다.")
    private APIResponse<ReviewCommentResDTO> createReview(@PathVariable Long reviewCommentId, @RequestBody ReviewCommentReqDTO reviewCommentReqDTO, Principal principal) {
        return reviewCommentService.fixReviewComment(reviewCommentId, reviewCommentReqDTO, principal);
    }

    //삭제
    @DeleteMapping("/{reviewCommentId}")
    @ApiOperation(value = "리뷰 댓글 작성", notes = "리뷰에 대한 댓글을 작성한 사용자가 자신의 대댓글을 삭제한다.")
    private APIResponse<ReviewCommentResDTO> deleteReview(@PathVariable Long reviewCommentId, Principal principal) {
        return reviewCommentService.deleteReviewComment(reviewCommentId, principal);
    }
}
