package kr.casealot.shop.domain.product.review.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import kr.casealot.shop.domain.customer.entity.Customer;
import kr.casealot.shop.domain.product.entity.Product;
import kr.casealot.shop.global.entity.BaseTimeEntity;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "REVIEW")
@Builder
public class Review extends BaseTimeEntity {
    @JsonIgnore
    @Id
    @Column(name = "REVIEW_SEQ")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PRODUCT_ID")
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CUSTOMER_ID")
    private Customer customer;

    @Column(name = "rating")
    private int rating; //별점

    @Column(name = "reviewText", length = 1024)
    private String reviewText; //리뷰 내용
}
