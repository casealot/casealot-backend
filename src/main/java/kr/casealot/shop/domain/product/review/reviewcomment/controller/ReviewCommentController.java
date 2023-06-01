package kr.casealot.shop.domain.product.review.reviewcomment.controller;

import io.swagger.annotations.Api;
import kr.casealot.shop.domain.product.review.reviewcomment.dto.ReviewCommentReqDTO;
import kr.casealot.shop.domain.product.review.reviewcomment.service.ReviewCommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
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
    private ResponseEntity<String> createReview(@RequestBody ReviewCommentReqDTO reviewCommentReqDTO, HttpServletRequest request, @PathVariable long reviewSeq) {
        reviewCommentService.createReviewComment(reviewCommentReqDTO, reviewSeq, request);
        return ResponseEntity.ok("create ok!");
    }

    //TODO: 리뷰 수정할 때 상품 아이디 들고와야하는데 일단 상품없어서 상품아이디 없이 박음. 주소도 바꿔야함
    //수정
    @PutMapping("/fix/{reviewCommentId}")
    private ResponseEntity<String> createReview(@PathVariable Long reviewCommentId, @RequestBody ReviewCommentReqDTO reviewCommentReqDTO, HttpServletRequest request) {
        reviewCommentService.fixReviewComment(reviewCommentId, reviewCommentReqDTO, request);
        return ResponseEntity.ok("fix ok!");
    }

    //TODO: 리뷰 삭제 때 상품 아이디 들고와야하는데 일단 상품없어서 상품아이디 없이 박음. 주소도 바꿔야함
    //삭제
    @DeleteMapping("/delete/{reviewCommentId}")
    private ResponseEntity<String> deleteReview(@PathVariable Long reviewCommentId, HttpServletRequest request) {
        reviewCommentService.deleteReviewComment(reviewCommentId, request);
        return ResponseEntity.ok("delete ok!");
    }
}
