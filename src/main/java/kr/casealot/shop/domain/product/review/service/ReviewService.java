package kr.casealot.shop.domain.product.review.service;

import kr.casealot.shop.domain.customer.entity.Customer;
import kr.casealot.shop.domain.customer.repository.CustomerRepository;
import kr.casealot.shop.domain.product.entity.Product;
import kr.casealot.shop.domain.product.repository.ProductRepository;
import kr.casealot.shop.domain.product.review.dto.ReviewReqDTO;
import kr.casealot.shop.domain.product.review.dto.ReviewResDTO;
import kr.casealot.shop.domain.product.review.entity.Review;
import kr.casealot.shop.domain.product.review.repository.ReviewRepository;
import kr.casealot.shop.domain.product.review.reviewcomment.service.ReviewCommentService;
import kr.casealot.shop.global.common.APIResponse;
import kr.casealot.shop.domain.product.exception.NoReviewException;
import kr.casealot.shop.global.exception.NotFoundProductException;
import kr.casealot.shop.global.exception.PermissionException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;

    private final ReviewCommentService reviewCommentService;

    public APIResponse<ReviewResDTO> createReview(ReviewReqDTO reviewReqDTO,  Long productId, Principal principal) {
        Customer customer = customerRepository.findById(principal.getName());

        Optional<Product> productOptional = productRepository.findById(productId);

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
            throw new NotFoundProductException();
        }
    }

    public APIResponse<ReviewResDTO> fixReview(Long reviewId, ReviewReqDTO reviewReqDTO, Principal principal) {
        Optional<Review> optionalReview = reviewRepository.findById(reviewId);
        if (optionalReview.isPresent()) {
            Review review = optionalReview.get();
            String reviewCustomerId = review.getCustomer().getId();
            if (principal.getName().equals(reviewCustomerId)) {
                review.setRating(reviewReqDTO.getRating());
                review.setReviewText(reviewReqDTO.getReviewText());
                reviewRepository.save(review);
            } else {
                throw new PermissionException();
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
            throw new PermissionException();
        }
    }

    public APIResponse<ReviewResDTO> deleteReview(Long reviewId, Principal principal) {
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
                throw new PermissionException();
            }
        } else {
            throw new PermissionException();
        }
    }

    @Transactional(readOnly = true)
    public APIResponse<ReviewResDTO> getReview(Long reviewSeq){
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
            log.warn("리뷰Optional이 존재하지 않음.");
            throw new NoReviewException();
        }
    }
}
