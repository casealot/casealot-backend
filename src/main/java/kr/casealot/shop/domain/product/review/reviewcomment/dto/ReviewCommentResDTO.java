package kr.casealot.shop.domain.product.review.reviewcomment.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import kr.casealot.shop.domain.product.review.reviewcomment.entity.ReviewComment;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewCommentResDTO {
    private Long id;
    private String customerName; //작성자 이름
    private String reviewCommentText; //리뷰 댓글 내용

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdDt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime modifiedDt;

    public static ReviewCommentResDTO createReviewCommentResDTO(ReviewComment reviewComment) {
        ReviewCommentResDTO reviewCommentResDTO = new ReviewCommentResDTO();
        reviewCommentResDTO.setId(reviewComment.getSeq());
        reviewCommentResDTO.setCustomerName(reviewComment.getCustomer().getName());
        reviewCommentResDTO.setReviewCommentText(reviewComment.getReviewCommentText());
        reviewCommentResDTO.setCreatedDt(reviewComment.getCreatedDt());
        reviewCommentResDTO.setModifiedDt(reviewComment.getModifiedDt());
        return reviewCommentResDTO;
    }

}
