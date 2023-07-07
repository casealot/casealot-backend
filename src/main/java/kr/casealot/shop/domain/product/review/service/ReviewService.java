package kr.casealot.shop.domain.product.review.service;

import kr.casealot.shop.domain.customer.entity.Customer;
import kr.casealot.shop.domain.customer.repository.CustomerRepository;
import kr.casealot.shop.domain.order.repository.OrderProductRepository;
import kr.casealot.shop.domain.order.repository.OrderRepository;
import kr.casealot.shop.domain.product.entity.Product;
import kr.casealot.shop.domain.product.exception.AlreadyReplyException;
import kr.casealot.shop.domain.product.exception.NoAuthToReplyException;
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
  private final OrderProductRepository orderProductRepository;
  private final ReviewCommentService reviewCommentService;

  @Transactional
  public APIResponse<ReviewResDTO> createReview(ReviewReqDTO reviewReqDTO, Long productId,
      Principal principal) {
    Customer customer = customerRepository.findById(principal.getName());

    Optional<Product> productOptional = Optional.ofNullable(productRepository.findById(productId)
        .orElseThrow(NotFoundProductException::new));
    if (!orderProductRepository.existsByProductIdAndCustomerSeq(productId, customer.getSeq())) {
      throw new NoAuthToReplyException(); //리뷰 작성권한 X
    }

    if (reviewRepository.existsByCustomerSeqAndProductId(customer.getSeq(), productId)) {
      throw new AlreadyReplyException(); //이미 리뷰 작성한 적 있음
    }

    Product product = productOptional.get();
    Review review = reviewRepository.save(Review.builder()
        .customer(customer)
        .product(product)
        .rating(reviewReqDTO.getRating())
        .reviewText(reviewReqDTO.getReviewText())
        .build());

    ReviewResDTO reviewResDTO = buildReviewResDTO(review);

    product.updateRating(reviewReqDTO.getRating());
    productRepository.saveAndFlush(product);

    return APIResponse.success("review", reviewResDTO);
  }

  @Transactional
  public APIResponse<ReviewResDTO> fixReview(Long reviewId, ReviewReqDTO reviewReqDTO,
      Principal principal) {
    Optional<Review> optionalReview = Optional.ofNullable(
        reviewRepository.findReviewBySeqAndCustomerId(reviewId,
            principal.getName()).orElseThrow(PermissionException::new));
    Review review = optionalReview.get();
    Double reviewOldRating = review.getRating();
    review.setRating(reviewReqDTO.getRating());
    review.setReviewText(reviewReqDTO.getReviewText());
    reviewRepository.save(review);

    Optional<Product> productOptional = Optional.ofNullable(
        productRepository.findById(review.getProduct().getId())
            .orElseThrow(NotFoundProductException::new));
    Product product = productOptional.get();

    product.fixRating(reviewOldRating, reviewReqDTO.getRating());
    productRepository.save(product);

    ReviewResDTO reviewResDTO = buildReviewResDTO(review);


    return APIResponse.success("review", reviewResDTO);
  }

  @Transactional
  public APIResponse<ReviewResDTO> deleteReview(Long reviewId, Principal principal) {
    Optional<Review> optionalReview = Optional.ofNullable(
        reviewRepository.findReviewBySeqAndCustomerId(reviewId,
            principal.getName()).orElseThrow(PermissionException::new));
    Review review = optionalReview.get();
    reviewRepository.delete(review);

    Optional<Product> productOptional = Optional.ofNullable(
        productRepository.findById(review.getProduct().getId())
            .orElseThrow(NotFoundProductException::new));
    Product product = productOptional.get();
    product.revertRating(optionalReview.get().getRating());
    productRepository.saveAndFlush(product);

    ReviewResDTO reviewResDTO = buildReviewResDTO(review);


    return APIResponse.success("review", reviewResDTO);

  }

  @Transactional(readOnly = true)
  public APIResponse<ReviewResDTO> getReview(Long reviewSeq) {
    Review review = reviewRepository.findById(reviewSeq).orElseThrow(NoReviewException::new);

      ReviewResDTO reviewResDTO = buildReviewResDTO(review);

      return APIResponse.success("review", reviewResDTO);

  }

  private ReviewResDTO buildReviewResDTO(Review review) {
    ReviewResDTO reviewResDTO = new ReviewResDTO();
    reviewResDTO.setId(review.getSeq());
    reviewResDTO.setCustomerName(review.getCustomer().getName());
    reviewResDTO.setRating(review.getRating());
    reviewResDTO.setReviewText(review.getReviewText());
    reviewResDTO.setReviewCommentList(reviewCommentService.getReviewCommentByReviewId(review.getSeq()));
    reviewResDTO.setCreatedDt(review.getCreatedDt());
    reviewResDTO.setModifiedDt(review.getModifiedDt());
    return reviewResDTO;
  }

}
