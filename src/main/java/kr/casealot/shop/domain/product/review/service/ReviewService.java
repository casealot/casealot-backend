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
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.swing.text.html.Option;
import java.security.Principal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReviewService {
    private final AuthTokenProvider authTokenProvider;
    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;

    private final ReviewCommentRepository reviewCommentRepository;
    private final ReviewCommentService reviewCommentService;

    public APIResponse<ReviewResDTO> createReview(ReviewReqDTO reviewReqDTO, HttpServletRequest request, Long id, Principal principal) {
        Customer customer = customerRepository.findById(principal.getName());

        Optional<Product> productOptional = productRepository.findById(id);

        if (productOptional.isPresent()) {
            Product product = productOptional.get();

            Review review = reviewRepository.save(Review.builder()
                    .customer(customer)
                    .product(product)
                    .rating(reviewReqDTO.getRating())
                    .reviewText(reviewReqDTO.getReviewText())
                    .build());

            ReviewResDTO reviewResDTO = new ReviewResDTO();
            reviewResDTO.setId(review.getSeq());
            reviewResDTO.setCustomerName(review.getCustomer().getName());
            reviewResDTO.setRating(review.getRating());
            reviewResDTO.setReviewText(review.getReviewText());
            reviewResDTO.setReviewCommentList(reviewCommentService.getReviewCommentByReviewId(review.getSeq()));
            reviewResDTO.setCreatedDt(review.getCreatedDt());
            reviewResDTO.setModifiedDt(review.getModifiedDt());

            return APIResponse.success("review", reviewResDTO);
        } else {
            return APIResponse.fail();
        }
    }

    public APIResponse<ReviewResDTO> fixReview(Long reviewId, ReviewReqDTO reviewReqDTO, HttpServletRequest request, Principal principal) {
        Optional<Review> optionalReview = reviewRepository.findById(reviewId);
        if (optionalReview.isPresent()) {
            Review review = optionalReview.get();
            String reviewCustomerId = review.getCustomer().getId();
            if (principal.getName().equals(reviewCustomerId)) {
                review.setRating(reviewReqDTO.getRating());
                review.setReviewText(reviewReqDTO.getReviewText());
                reviewRepository.save(review);
            } else {
                return APIResponse.permissionDenied();
            }
            ReviewResDTO reviewResDTO = new ReviewResDTO();
            reviewResDTO.setId(review.getSeq());
            reviewResDTO.setCustomerName(principal.getName());
            reviewResDTO.setRating(review.getRating());
            reviewResDTO.setReviewText(review.getReviewText());
            reviewResDTO.setReviewCommentList(reviewCommentService.getReviewCommentByReviewId(review.getSeq()));
            reviewResDTO.setCreatedDt(review.getCreatedDt());
            reviewResDTO.setModifiedDt(review.getModifiedDt());

            return APIResponse.success("review", reviewResDTO);
        } else {
            return APIResponse.permissionDenied();
        }
    }

    public APIResponse<ReviewResDTO> deleteReview(Long reviewId, HttpServletRequest request, Principal principal) {
        Optional<Review> optionalReview = reviewRepository.findById(reviewId);
        if (optionalReview.isPresent()) {
            Review review = optionalReview.get();
            String reviewCustomerId = review.getCustomer().getId();
            if (principal.getName().equals(reviewCustomerId)) {
                reviewRepository.delete(review);
                ReviewResDTO reviewResDTO = new ReviewResDTO();
                reviewResDTO.setId(review.getSeq());
                reviewResDTO.setCustomerName(principal.getName());
                reviewResDTO.setRating(review.getRating());
                reviewResDTO.setReviewText(review.getReviewText());
                reviewResDTO.setReviewCommentList(reviewCommentService.getReviewCommentByReviewId(review.getSeq()));
                reviewResDTO.setCreatedDt(review.getCreatedDt());
                reviewResDTO.setModifiedDt(review.getModifiedDt());

                return APIResponse.success("review", reviewResDTO);
            } else {
                return APIResponse.permissionDenied();
            }
        } else {
            return APIResponse.permissionDenied();
        }
    }

    @Transactional(readOnly = true)
    public APIResponse<ReviewResDTO> getReview(Long reviewSeq, Principal principal){
        Optional<Review> reviewOptional = reviewRepository.findById(reviewSeq);

        if (reviewOptional.isPresent()) {
            Review review = reviewOptional.get();

            ReviewResDTO reviewResDTO = new ReviewResDTO();
            reviewResDTO.setId(review.getSeq());
            reviewResDTO.setCustomerName(review.getCustomer().getName());
            reviewResDTO.setRating(review.getRating());
            reviewResDTO.setReviewText(review.getReviewText());
            reviewResDTO.setReviewCommentList(reviewCommentService.getReviewCommentByReviewId(review.getSeq()));
            reviewResDTO.setCreatedDt(review.getCreatedDt());
            reviewResDTO.setModifiedDt(review.getModifiedDt());

            return APIResponse.success("review", reviewResDTO);
        } else {
            // Optional이 비어있을 경우에 대한 처리
            log.warn("이건 오류다.");
            return APIResponse.notExistRequest();
        }
    }
}
