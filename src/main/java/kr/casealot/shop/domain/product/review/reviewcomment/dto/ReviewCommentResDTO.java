package kr.casealot.shop.domain.product.review.reviewcomment.dto;

import kr.casealot.shop.domain.customer.entity.Customer;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
@Setter
public class ReviewCommentResDTO {
    private String customerName; //작성자 이름
    private String reviewCommentText; //리뷰 댓글 내용
}
