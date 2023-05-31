package kr.casealot.shop.domain.product.review.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import kr.casealot.shop.domain.customer.entity.Customer;
import kr.casealot.shop.domain.product.review.reviewcomment.entity.ReviewComment;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ReviewResDTO {
    private String customerName; //고객 이름
    private Double rating; //별점
    private String reviewText; //리뷰 내용
    @JsonProperty
    private List<ReviewComment> reviewCommentList;
}
