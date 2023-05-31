package kr.casealot.shop.domain.product.review.service;

import io.jsonwebtoken.Claims;
import kr.casealot.shop.domain.customer.entity.Customer;
import kr.casealot.shop.domain.customer.repository.CustomerRepository;
import kr.casealot.shop.domain.product.entity.Product;
import kr.casealot.shop.domain.product.repository.ProductRepository;
import kr.casealot.shop.domain.product.review.dto.ReviewReqDTO;
import kr.casealot.shop.domain.product.review.dto.ReviewResDTO;
import kr.casealot.shop.domain.product.review.entity.Review;
import kr.casealot.shop.domain.product.review.repository.ReviewRepository;
import kr.casealot.shop.domain.product.review.reviewcomment.entity.ReviewComment;
import kr.casealot.shop.domain.product.review.reviewcomment.repository.ReviewCommentRepository;
import kr.casealot.shop.domain.qna.comment.entity.QnaComment;
import kr.casealot.shop.domain.qna.entity.Qna;
import kr.casealot.shop.global.oauth.token.AuthToken;
import kr.casealot.shop.global.oauth.token.AuthTokenProvider;
import kr.casealot.shop.global.util.HeaderUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final AuthTokenProvider authTokenProvider;
    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;
    private final ReviewCommentRepository reviewCommentRepository;

    public void createReview(ReviewReqDTO reviewReqDTO, HttpServletRequest request) {
        String customerId = findCustomerId(request);

        Customer customer = customerRepository.findById(customerId);
        reviewRepository.save(Review.builder()
                .customer(customer)
                .rating(reviewReqDTO.getRating())
                .reviewText(reviewReqDTO.getReviewText())
                .build());
    }

    public void fixReview(Long reviewId, ReviewReqDTO reviewReqDTO, HttpServletRequest request) {
        String customerId = findCustomerId(request);

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
        String customerId = findCustomerId(request);

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

//    public ReviewResDTO getReviewByReviewSeq(Long reviewSeq) {
////        // 상품 ID를 사용하여 해당 상품의 리뷰 정보 조회
////        Product product = getProductById(productId);
//
////        // 리뷰 정보 가져오기
////        Review review = reviewRepository.findByProduct(product);
//        Review review = reviewRepository.findBySeq(reviewSeq);
//
//        ReviewResDTO reviewResDTO = new ReviewResDTO();
//        reviewResDTO.setCustomer(review.getCustomer());
//        reviewResDTO.setRating(review.getRating());
//        reviewResDTO.setReviewText(review.getReviewText());
//        reviewResDTO.setReviewCommentList(review.getReviewCommentList());
//
//        // reviewCommentIds 설정
//
//        return reviewResDTO;
//    }

    // qna 조회
    @Transactional
    public Review getReview(Long reviewSeq) throws ChangeSetPersister.NotFoundException {
        Review review = reviewRepository.findById(reviewSeq).orElseThrow(ChangeSetPersister.NotFoundException::new);
        List<ReviewComment> commentList = reviewCommentRepository.findByReviewSeq(reviewSeq);
        review.setReviewCommentList(commentList);
        reviewRepository.save(review);
        return review;
    }

    private String findCustomerId(HttpServletRequest request) {
        String token = HeaderUtil.getAccessToken(request);
        AuthToken authToken = authTokenProvider.convertAuthToken(token);
        Claims claims = authToken.getTokenClaims();
        return claims.getSubject();
    }
}
