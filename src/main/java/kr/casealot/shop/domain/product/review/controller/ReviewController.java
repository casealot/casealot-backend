package kr.casealot.shop.domain.product.review.controller;

import io.swagger.annotations.Api;
import kr.casealot.shop.domain.product.review.dto.ReviewReqDTO;
import kr.casealot.shop.domain.product.review.dto.ReviewResDTO;
import kr.casealot.shop.domain.product.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.ResponseEntity;
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

    //TODO: 리뷰 작성할 때 상품 아이디 들고와야하는데 일단 상품없어서 상품아이디 없이 박음. 주소도 바꿔야함
    //생성
    //@PostMapping("/{productId}/create")
    @PostMapping("/create")
    private ResponseEntity<String> createReview(@RequestBody ReviewReqDTO reviewReqDTO, HttpServletRequest request,@PathVariable Long id) {
        reviewService.createReview(reviewReqDTO, request, id);
        return ResponseEntity.ok("create ok!");
    }

    //TODO: 리뷰 수정할 때 상품 아이디 들고와야하는데 일단 상품없어서 상품아이디 없이 박음. 주소도 바꿔야함
    //수정
    //@PutMapping("/{productId}/fix/{reviewId}")
    @PutMapping("/fix/{reviewId}")
    private ResponseEntity<String> createReview(@PathVariable Long reviewId, @RequestBody ReviewReqDTO reviewReqDTO, HttpServletRequest request) {
        reviewService.fixReview(reviewId,reviewReqDTO, request);
        return ResponseEntity.ok("fix ok!");
    }

    //TODO: 리뷰 삭제 때 상품 아이디 들고와야하는데 일단 상품없어서 상품아이디 없이 박음. 주소도 바꿔야함
    //삭제
    //@DeleteMapping("/{productId}/delete/{reviewId}")
    @DeleteMapping("/delete/{reviewId}")
    private ResponseEntity<String> deleteReview(@PathVariable Long reviewId, HttpServletRequest request) {
        reviewService.deleteReview(reviewId, request);
        return ResponseEntity.ok("delete ok!");
    }

    @GetMapping("/view/{reviewId}")
    private ResponseEntity<ReviewResDTO> viewReview(@PathVariable Long reviewId) throws ChangeSetPersister.NotFoundException {
        ReviewResDTO review = reviewService.getReview(reviewId);
        return ResponseEntity.ok(review);
    }
}