package kr.casealot.shop.domain.product.review.reviewcomment.dto;

import lombok.*;

@Getter
@Builder
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewCommentResDTO {
    private String customerName; //작성자 이름
    private String reviewCommentText; //리뷰 댓글 내용
}
