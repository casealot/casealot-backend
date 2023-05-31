package kr.casealot.shop.domain.product.review.controller;

import kr.casealot.shop.domain.product.review.dto.ReviewReqDTO;
import kr.casealot.shop.domain.product.review.dto.ReviewResDTO;
import kr.casealot.shop.domain.product.review.entity.Review;
import kr.casealot.shop.domain.product.review.repository.ReviewRepository;
import kr.casealot.shop.domain.product.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/cal/v1/review")
public class ReviewController {
    private final ReviewRepository reviewRepository;
    private final ReviewService reviewService;

    //TODO: 리뷰 작성할 때 상품 아이디 들고와야하는데 일단 상품없어서 상품아이디 없이 박음. 주소도 바꿔야함
    //생성
    //@PostMapping("/{productId}/create")
    @PostMapping("/create")
    private ResponseEntity<String> createReview(@RequestBody ReviewReqDTO reviewReqDTO, HttpServletRequest request) {
        reviewService.createReview(reviewReqDTO, request);
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