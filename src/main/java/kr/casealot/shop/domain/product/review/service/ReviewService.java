package kr.casealot.shop.domain.product.review.service;

import io.jsonwebtoken.Claims;
import kr.casealot.shop.domain.customer.dto.CustomerDto;
import kr.casealot.shop.domain.customer.entity.Customer;
import kr.casealot.shop.domain.customer.repository.CustomerRepository;
import kr.casealot.shop.domain.product.repository.ProductRepository;
import kr.casealot.shop.domain.product.review.dto.ReviewReqDTO;
import kr.casealot.shop.domain.product.review.entity.Review;
import kr.casealot.shop.domain.product.review.repository.ReviewRepository;
import kr.casealot.shop.global.oauth.token.AuthToken;
import kr.casealot.shop.global.oauth.token.AuthTokenProvider;
import kr.casealot.shop.global.util.HeaderUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final AuthTokenProvider authTokenProvider;
    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;

    public void createReview(ReviewReqDTO reviewReqDTO, HttpServletRequest request) {
        // 토큰 파싱
        String token = HeaderUtil.getAccessToken(request);
        AuthToken authToken = authTokenProvider.convertAuthToken(token);
        Claims claims = authToken.getTokenClaims();
        String customerId = claims.getSubject();

        Customer customer = customerRepository.findById(customerId);
        reviewRepository.save(Review.builder()
                .customer(customer)
                .rating(reviewReqDTO.getRating())
                .reviewText(reviewReqDTO.getReviewText())
                .build());
    }

    public void fixReview(Long reviewId, ReviewReqDTO reviewReqDTO, HttpServletRequest request) {
        String token = HeaderUtil.getAccessToken(request);
        AuthToken authToken = authTokenProvider.convertAuthToken(token);
        Claims claims = authToken.getTokenClaims();
        String customerId = claims.getSubject();

        Optional<Review> optionalReview = reviewRepository.findById(reviewId);
        if (optionalReview.isPresent()) {
            Review review = optionalReview.get();
            String reviewCustomerId = review.getCustomer().getId();
            if (customerId.equals(reviewCustomerId)) {
                review.setRating(reviewReqDTO.getRating());
                review.setReviewText(reviewReqDTO.getReviewText());
                reviewRepository.save(review);
            } else {
                throw new IllegalArgumentException("Unauthorized: You are not allowed to modify this review.");
            }
        } else {
            throw new IllegalArgumentException("Review not found with ID: " + reviewId);
        }
    }

    public void deleteReview(Long reviewId, HttpServletRequest request) {
        String token = HeaderUtil.getAccessToken(request);
        AuthToken authToken = authTokenProvider.convertAuthToken(token);
        Claims claims = authToken.getTokenClaims();
        String customerId = claims.getSubject();

        Optional<Review> optionalReview = reviewRepository.findById(reviewId);
        if (optionalReview.isPresent()) {
            Review review = optionalReview.get();
            String reviewCustomerId = review.getCustomer().getId();
            if (customerId.equals(reviewCustomerId)) {
                reviewRepository.delete(review);
            } else {
                throw new IllegalArgumentException("Unauthorized: You are not allowed to delete this review.");
            }
        } else {
            throw new IllegalArgumentException("Review not found with ID: " + reviewId);
        }
    }
}
