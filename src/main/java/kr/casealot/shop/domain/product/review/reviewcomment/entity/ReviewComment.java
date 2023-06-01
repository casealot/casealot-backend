package kr.casealot.shop.domain.product.review.reviewcomment.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import kr.casealot.shop.domain.customer.entity.Customer;
import kr.casealot.shop.domain.product.review.entity.Review;
import kr.casealot.shop.global.entity.BaseTimeEntity;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "REVIEW_COMMENT")
@Builder
public class ReviewComment extends BaseTimeEntity {
    @JsonIgnore
    @Id
    @Column(name = "REVIEW_COMMENT_SEQ")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @JsonManagedReference
    @ManyToOne
    @JoinColumn(name = "CUSTOMER_ID")
    private Customer customer;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "REVIEW_ID")
    private Review review;

    @Column(name = "REVIEW_COMMENT_TEXT", length = 1024)
    private String reviewCommentText; //리뷰 댓글 내용
}
