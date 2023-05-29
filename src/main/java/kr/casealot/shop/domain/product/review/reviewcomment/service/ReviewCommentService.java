package kr.casealot.shop.domain.product.review.reviewcomment.service;

import io.jsonwebtoken.Claims;
import kr.casealot.shop.domain.customer.entity.Customer;
import kr.casealot.shop.domain.customer.repository.CustomerRepository;
import kr.casealot.shop.domain.product.repository.ProductRepository;
import kr.casealot.shop.domain.product.review.dto.ReviewReqDTO;
import kr.casealot.shop.domain.product.review.entity.Review;
import kr.casealot.shop.domain.product.review.repository.ReviewRepository;
import kr.casealot.shop.domain.product.review.reviewcomment.dto.ReviewCommentReqDTO;
import kr.casealot.shop.domain.product.review.reviewcomment.entity.ReviewComment;
import kr.casealot.shop.domain.product.review.reviewcomment.repository.ReviewCommentRepository;
import kr.casealot.shop.global.oauth.token.AuthToken;
import kr.casealot.shop.global.oauth.token.AuthTokenProvider;
import kr.casealot.shop.global.util.HeaderUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReviewCommentService {
    private final AuthTokenProvider authTokenProvider;
    private final ReviewCommentRepository reviewCommentRepository;
    private final CustomerRepository customerRepository;
    private final ReviewRepository reviewRepository;

    public void createReviewComment(ReviewCommentReqDTO reviewCommentReqDTO, Long reviewSeq, HttpServletRequest request) {
        String customerId = findCustomerId(request);

        Customer customer = customerRepository.findById(customerId);
        Review review = reviewRepository.findBySeq(reviewSeq);
        reviewCommentRepository.save(ReviewComment.builder()
                .customer(customer)
                .review(review)
                .reviewCommentText(reviewCommentReqDTO.getReviewCommentText())
                .build());
    }

    public void fixReviewComment(Long reviewCommentId, ReviewCommentReqDTO reviewCommentReqDTO, HttpServletRequest request) {
        String customerId = findCustomerId(request);

        Optional<ReviewComment> optionalReviewComment = reviewCommentRepository.findById(reviewCommentId);
        if (optionalReviewComment.isPresent()) {
            ReviewComment review = optionalReviewComment.get();
            String reviewCustomerId = review.getCustomer().getId();
            if (customerId.equals(reviewCustomerId)) {
                review.setReviewCommentText(reviewCommentReqDTO.getReviewCommentText());
                reviewCommentRepository.save(review);
            } else {
                throw new IllegalArgumentException("Unauthorized: You are not allowed to delete this review comment.");
            }
        } else {
            throw new IllegalArgumentException("Review Comment not found with ID: " + reviewCommentId);
        }
    }

    public void deleteReviewComment(Long reviewCommentId, HttpServletRequest request) {
        String customerId = findCustomerId(request);

        Optional<ReviewComment> optionalReviewComment = reviewCommentRepository.findById(reviewCommentId);
        if (optionalReviewComment.isPresent()) {
            ReviewComment reviewComment = optionalReviewComment.get();
            String reviewCommentCustomerId = reviewComment.getCustomer().getId();
            if (customerId.equals(reviewCommentCustomerId)) {
                reviewCommentRepository.delete(reviewComment);
            } else {
                throw new IllegalArgumentException("Unauthorized: You are not allowed to delete this review comment.");
            }
        } else {
            throw new IllegalArgumentException("Review Comment not found with ID: " + reviewCommentId);
        }

    }

    private String findCustomerId(HttpServletRequest request) {
        String token = HeaderUtil.getAccessToken(request);
        AuthToken authToken = authTokenProvider.convertAuthToken(token);
        Claims claims = authToken.getTokenClaims();
        return claims.getSubject();
    }
}
