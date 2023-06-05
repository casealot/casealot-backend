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
import kr.casealot.shop.domain.product.review.reviewcomment.repository.ReviewCommentRepository;
import kr.casealot.shop.domain.product.review.reviewcomment.service.ReviewCommentService;
import kr.casealot.shop.global.common.APIResponse;
import kr.casealot.shop.global.oauth.token.AuthToken;
import kr.casealot.shop.global.oauth.token.AuthTokenProvider;
import kr.casealot.shop.global.util.HeaderUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final AuthTokenProvider authTokenProvider;
    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;

    private final ReviewCommentRepository reviewCommentRepository;
    private final ReviewCommentService reviewCommentService;

    public APIResponse<Void> createReview(ReviewReqDTO reviewReqDTO, HttpServletRequest request, Long id) {
        String customerId = findCustomerId(request);

        Customer customer = customerRepository.findById(customerId);

        Optional<Product> productOptional = productRepository.findById(id);

        if (productOptional.isPresent()) {
            Product product = productOptional.get();

            reviewRepository.save(Review.builder()
                    .customer(customer)
                    .product(product)
                    .rating(reviewReqDTO.getRating())
                    .reviewText(reviewReqDTO.getReviewText())
                    .build());
            return APIResponse.success("리뷰 작성 완료",null);
        }else{
            return APIResponse.fail();
        }
    }

    public APIResponse<Void> fixReview(Long reviewId, ReviewReqDTO reviewReqDTO, HttpServletRequest request) {
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
                return APIResponse.permissionDenied();
            }
            return APIResponse.success("리뷰 수정 완료",null);
        } else {
            return APIResponse.permissionDenied();
        }
    }

    public APIResponse<Void> deleteReview(Long reviewId, HttpServletRequest request) {
        String customerId = findCustomerId(request);

        Optional<Review> optionalReview = reviewRepository.findById(reviewId);
        if (optionalReview.isPresent()) {
            Review review = optionalReview.get();
            String reviewCustomerId = review.getCustomer().getId();
            if (customerId.equals(reviewCustomerId)) {
                reviewRepository.delete(review);
            } else {
                return APIResponse.permissionDenied();
            }
        } else {
            return APIResponse.permissionDenied();
        }
        return APIResponse.success("리뷰 삭제 완료", null);
    }

    @Transactional(readOnly = true)
    public APIResponse<ReviewResDTO> getReview(Long reviewSeq) throws ChangeSetPersister.NotFoundException {
        Review review = reviewRepository.findById(reviewSeq)
                .orElseThrow(ChangeSetPersister.NotFoundException::new);

        ReviewResDTO reviewResDTO = new ReviewResDTO();
        reviewResDTO.setCustomerName(review.getCustomer().getName());
        reviewResDTO.setRating(review.getRating());
        reviewResDTO.setReviewText(review.getReviewText());
        reviewResDTO.setReviewCommentList(reviewCommentService.getReviewCommentByReviewId(reviewSeq));

        return APIResponse.success("review",reviewResDTO);
    }

    private String findCustomerId(HttpServletRequest request) {
        String token = HeaderUtil.getAccessToken(request);
        AuthToken authToken = authTokenProvider.convertAuthToken(token);
        Claims claims = authToken.getTokenClaims();
        return claims.getSubject();
    }
}
