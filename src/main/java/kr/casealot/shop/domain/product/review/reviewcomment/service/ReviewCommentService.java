package kr.casealot.shop.domain.product.review.reviewcomment.service;

import io.jsonwebtoken.Claims;
import kr.casealot.shop.domain.customer.entity.Customer;
import kr.casealot.shop.domain.customer.repository.CustomerRepository;
import kr.casealot.shop.domain.product.review.entity.Review;
import kr.casealot.shop.domain.product.review.repository.ReviewRepository;
import kr.casealot.shop.domain.product.review.reviewcomment.dto.ReviewCommentReqDTO;
import kr.casealot.shop.domain.product.review.reviewcomment.dto.ReviewCommentResDTO;
import kr.casealot.shop.domain.product.review.reviewcomment.entity.ReviewComment;
import kr.casealot.shop.domain.product.review.reviewcomment.repository.ReviewCommentRepository;
import kr.casealot.shop.global.common.APIResponse;
import kr.casealot.shop.global.oauth.token.AuthToken;
import kr.casealot.shop.global.oauth.token.AuthTokenProvider;
import kr.casealot.shop.global.util.HeaderUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewCommentService {
    private final AuthTokenProvider authTokenProvider;
    private final ReviewCommentRepository reviewCommentRepository;
    private final CustomerRepository customerRepository;
    private final ReviewRepository reviewRepository;

    public APIResponse<Void> createReviewComment(ReviewCommentReqDTO reviewCommentReqDTO, Long reviewSeq, HttpServletRequest request) {
        String customerId = findCustomerId(request);

        Customer customer = customerRepository.findById(customerId);
        Review review = reviewRepository.findBySeq(reviewSeq);
        if (review == null) {
            return APIResponse.notExistRequest();
        }
        reviewCommentRepository.save(ReviewComment.builder()
                .customer(customer)
                .review(review)
                .reviewCommentText(reviewCommentReqDTO.getReviewCommentText())
                .build());

        return APIResponse.success("리뷰 댓글 작성 성공", null);
    }

    public APIResponse<Void> fixReviewComment(Long reviewCommentId, ReviewCommentReqDTO reviewCommentReqDTO, HttpServletRequest request) {
        String customerId = findCustomerId(request);

        Optional<ReviewComment> optionalReviewComment = reviewCommentRepository.findById(reviewCommentId);
        if (optionalReviewComment.isPresent()) {
            ReviewComment review = optionalReviewComment.get();
            String reviewCustomerId = review.getCustomer().getId();
            if (customerId.equals(reviewCustomerId)) {
                review.setReviewCommentText(reviewCommentReqDTO.getReviewCommentText());
                reviewCommentRepository.save(review);
                return APIResponse.success("리뷰 댓글 수정 성공", null);
            } else {
                return APIResponse.permissionDenied();
            }
        } else {
            return APIResponse.notExistRequest();
        }
    }

    public APIResponse<Void> deleteReviewComment(Long reviewCommentId, HttpServletRequest request) {
        String customerId = findCustomerId(request);

        Optional<ReviewComment> optionalReviewComment = reviewCommentRepository.findById(reviewCommentId);
        if (optionalReviewComment.isPresent()) {
            ReviewComment reviewComment = optionalReviewComment.get();
            String reviewCommentCustomerId = reviewComment.getCustomer().getId();
            if (customerId.equals(reviewCommentCustomerId)) {
                reviewCommentRepository.delete(reviewComment);
                return APIResponse.success("리뷰 댓글 삭제 성공", null);
            } else {
                return APIResponse.permissionDenied();
            }
        } else {
            return APIResponse.notExistRequest();
        }
    }

    public List<ReviewCommentResDTO> getReviewCommentByReviewId(Long reviewId) {
        List<ReviewComment> reviewComments = reviewCommentRepository.findByReviewSeq(reviewId);
        return reviewComments.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    private ReviewCommentResDTO mapToDto(ReviewComment reviewComment) {
        return ReviewCommentResDTO.builder()
                .customerName(reviewComment.getCustomer().getName())
                .reviewCommentText(reviewComment.getReviewCommentText())
                .build();
    }

    private String findCustomerId(HttpServletRequest request) {
        String token = HeaderUtil.getAccessToken(request);
        AuthToken authToken = authTokenProvider.convertAuthToken(token);
        Claims claims = authToken.getTokenClaims();
        return claims.getSubject();
    }
}
