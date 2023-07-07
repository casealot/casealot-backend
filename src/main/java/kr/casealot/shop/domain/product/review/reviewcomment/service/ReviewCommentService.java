package kr.casealot.shop.domain.product.review.reviewcomment.service;

import static kr.casealot.shop.domain.product.review.reviewcomment.dto.ReviewCommentResDTO.createReviewCommentResDTO;

import kr.casealot.shop.domain.customer.entity.Customer;
import kr.casealot.shop.domain.customer.repository.CustomerRepository;
import kr.casealot.shop.domain.product.review.entity.Review;
import kr.casealot.shop.domain.product.review.repository.ReviewRepository;
import kr.casealot.shop.domain.product.review.reviewcomment.dto.ReviewCommentReqDTO;
import kr.casealot.shop.domain.product.review.reviewcomment.dto.ReviewCommentResDTO;
import kr.casealot.shop.domain.product.review.reviewcomment.entity.ReviewComment;
import kr.casealot.shop.domain.product.review.reviewcomment.repository.ReviewCommentRepository;
import kr.casealot.shop.global.common.APIResponse;
import kr.casealot.shop.domain.product.exception.NoReviewException;
import kr.casealot.shop.global.exception.PermissionException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewCommentService {

  private final ReviewCommentRepository reviewCommentRepository;
  private final CustomerRepository customerRepository;
  private final ReviewRepository reviewRepository;

  public APIResponse<ReviewCommentResDTO> createReviewComment(
      ReviewCommentReqDTO reviewCommentReqDTO, Long reviewSeq, Principal principal) {
    Customer customer = customerRepository.findById(principal.getName());
    Review review = reviewRepository.findBySeq(reviewSeq);
    if (review == null) {
      throw new NoReviewException();
    }
    ReviewComment reviewComment = reviewCommentRepository.save(ReviewComment.builder()
        .customer(customer)
        .review(review)
        .reviewCommentText(reviewCommentReqDTO.getReviewCommentText())
        .build());

    ReviewCommentResDTO reviewCommentRes = createReviewCommentResDTO(reviewComment);
    return APIResponse.success("reviewComment", reviewCommentRes);
  }

  public APIResponse<ReviewCommentResDTO> fixReviewComment(Long reviewCommentId,
      ReviewCommentReqDTO reviewCommentReqDTO, Principal principal) {
    Optional<ReviewComment> optionalReviewComment = reviewCommentRepository.findById(
        reviewCommentId);
    if (optionalReviewComment.isPresent()) {
      ReviewComment review = optionalReviewComment.get();
      String reviewCustomerId = review.getCustomer().getId();
      if (principal.getName().equals(reviewCustomerId)) {
        review.setReviewCommentText(reviewCommentReqDTO.getReviewCommentText());
        ReviewComment reviewComment = reviewCommentRepository.save(review);
        ReviewCommentResDTO reviewCommentRes = createReviewCommentResDTO(reviewComment);

        return APIResponse.success("reviewComment", reviewCommentRes);
      } else {
        throw new PermissionException();
      }
    } else {
      throw new PermissionException();
    }
  }

  public APIResponse<ReviewCommentResDTO> deleteReviewComment(Long reviewCommentId,
      Principal principal) {
    Optional<ReviewComment> optionalReviewComment = reviewCommentRepository.findById(
        reviewCommentId);
    if (optionalReviewComment.isPresent()) {
      ReviewComment reviewComment = optionalReviewComment.get();
      String reviewCommentCustomerId = reviewComment.getCustomer().getId();
      if (principal.getName().equals(reviewCommentCustomerId)) {
        reviewCommentRepository.delete(reviewComment);
        ReviewCommentResDTO reviewCommentRes = createReviewCommentResDTO(reviewComment);

        return APIResponse.success("reviewComment", reviewCommentRes);
      } else {
        throw new PermissionException();
      }
    } else {
      throw new PermissionException();
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
        .id(reviewComment.getSeq())
        .customerName(reviewComment.getCustomer().getName())
        .reviewCommentText(reviewComment.getReviewCommentText())
        .createdDt(reviewComment.getCreatedDt())
        .modifiedDt(reviewComment.getModifiedDt())
        .build();
  }
}
