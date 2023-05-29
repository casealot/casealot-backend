package kr.casealot.shop.domain.product.review.reviewcomment.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@NoArgsConstructor
public class ReviewCommentReqDTO {
    private String reviewCommentText; //댓글 내용
}
