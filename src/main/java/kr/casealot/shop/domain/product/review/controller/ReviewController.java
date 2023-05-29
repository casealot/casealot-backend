package kr.casealot.shop.domain.product.review.controller;

import kr.casealot.shop.domain.product.review.dto.ReviewReqDTO;
import kr.casealot.shop.domain.product.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/cal/v1/review")
public class ReviewController {
    private final ReviewService reviewService;

    //TODO: 리뷰 작성할 때 상품 아이디 들고와야하는데 일단 상품없어서 상품아이디 없이 박음. 주소도 바꿔야함
    @PostMapping("/create")
    private ResponseEntity<String> createReview(@RequestBody ReviewReqDTO reviewReqDTO, HttpServletRequest request) {
        System.out.println("!!!");
        log.info(String.valueOf(reviewReqDTO));
        System.out.println("!!!");

        reviewService.createReview(reviewReqDTO, request);
        return ResponseEntity.ok("ohgay");
    }
}
