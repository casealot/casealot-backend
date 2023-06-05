package kr.casealot.shop.domain.product.review.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import kr.casealot.shop.domain.product.review.entity.Review;
import kr.casealot.shop.domain.product.review.reviewcomment.dto.ReviewCommentResDTO;
import kr.casealot.shop.domain.product.review.reviewcomment.entity.ReviewComment;
import lombok.*;
import org.hibernate.Hibernate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReviewResDTO {
    private String customerName; //고객 이름
    private Double rating; //별점
    private String reviewText; //리뷰 내용
    @JsonProperty
    private List<ReviewCommentResDTO> reviewCommentList;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdDt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime modifiedDt;

    public static ReviewResDTO fromReview(Review review) {
        ReviewResDTO reviewResDTO = new ReviewResDTO();

        reviewResDTO.setCustomerName(review.getCustomer().getName());
        reviewResDTO.setRating(review.getRating());
        reviewResDTO.setReviewText(review.getReviewText());
        reviewResDTO.setCreatedDt(review.getCreatedDt());
        reviewResDTO.setModifiedDt(review.getModifiedDt());

        // 연관 엔티티 로딩
        Hibernate.initialize(review.getReviewCommentList());

        List<ReviewCommentResDTO> reviewCommentResList = new ArrayList<>();

        for (ReviewComment reviewComment : review.getReviewCommentList()) {
            ReviewCommentResDTO reviewCommentResDTO = new ReviewCommentResDTO();
            reviewCommentResDTO.setCustomerName(reviewComment.getCustomer().getName());
            reviewCommentResDTO.setReviewCommentText(reviewComment.getReviewCommentText());
            reviewCommentResDTO.setCreatedDt(reviewComment.getCreatedDt());
            reviewCommentResDTO.setModifiedDt(reviewComment.getModifiedDt());
            reviewCommentResList.add(reviewCommentResDTO);
        }

        reviewResDTO.setReviewCommentList(reviewCommentResList);

        return reviewResDTO;
    }
}
