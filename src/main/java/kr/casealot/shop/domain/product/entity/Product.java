package kr.casealot.shop.domain.product.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import kr.casealot.shop.domain.customer.entity.Customer;
import kr.casealot.shop.domain.file.entity.UploadFile;
import kr.casealot.shop.domain.product.review.entity.Review;
import kr.casealot.shop.global.entity.BaseTimeEntity;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "PRODUCT", indexes = {
        @Index(name = "idx_product_id", columnList = "PRODUCT_ID"),
        @Index(name = "idx_product_name", columnList = "PRODUCT_NAME"),
        @Index(name = "idx_product_type", columnList = "PRODUCT_TYPE"),
        @Index(name = "idx_product_color", columnList = "PRODUCT_COLOR"),
})
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Slf4j
public class Product extends BaseTimeEntity {
    @Id
    @GeneratedValue
    @Column(name = "PRODUCT_ID")
    private Long id;

    @JsonIgnore
    @JsonBackReference
    @ManyToOne
    private Customer customer;

    @Column(name = "PRODUCT_NAME", length = 512)
    private String name;
    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "PRODUCT_CONTENT")
    private String content;

    // 상품 이미지 프리뷰
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private UploadFile thumbnail;

    // 상품 > 디테일 이미지
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<UploadFile> images = new ArrayList<>();

    @Column(name = "PRODUCT_PRICE")
    private int price;

    @Column(name = "PRODUCT_SALE")
    private int sale;

    @Column(name = "PRODUCT_VIEWS")
    private int views;

    @Column(name = "PRODUCT_SELLS")
    private int sells;

    @Column(name = "PRODUCT_COLOR", length = 50)
    private String color;

    @Column(name = "PRODUCT_SEASON", length = 50)
    private String season;

    @Column(name = "PRODUCT_TYPE", length = 50)
    private String type;

    @Column(name = "PRODUCT_RATING")
    private Double rating; // 상품 별점

    @Column(name = "PRODUCT_TOTAL_RATING")
    private Double totalRating; // 상품 총 별점

    @Column(name = "PRODUCT_RATING_COUNT")
    private Double ratingCount; // 별점 준 횟수

    @Column(name = "PRODUCT_LIKE")
    private Integer wishCount;

    @Column(name = "PRODUCT_CATEGORY")
    private String category;

    @Column(name = "PRODUCT_CALCULATE_PRICE")
    private Double calculatePrice;

    //상품 1개에 리뷰 n개 (1:n 설정)
    @JsonIgnore
    @JsonBackReference
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<Review> reviews = new ArrayList<>();

    @Builder
    public Product(String name, String content, int price, int sale,
                   String color, String season, String type) {
        this.name = name;
        this.content = content;
        this.price = price;
        this.sale = sale;
        this.color = color;
        this.season = season;
        this.type = type;
    }

    //조회수 증가
    public void addView(int views) {
        this.views += views;
    }

    //별점 계산
    public void updateRating(Double newRating) {
        // 이전 총 별점수와 별점을 준 횟수를 가져옵니다.
        double oldTotalRating = getTotalRating() != null ? getTotalRating() : 0.0;
        double oldRatingCount = getRatingCount() != null ? getRatingCount() : 0.0;

        // 새로운 총 별점수와 별점을 준 횟수를 계산합니다.
        double newTotalRating = oldTotalRating + newRating;
        double newRatingCount = oldRatingCount + 1;

        // 총 별점수와 별점을 준 횟수를 업데이트합니다.
        setTotalRating(newTotalRating);
        setRatingCount(newRatingCount);
        setRating(newTotalRating / newRatingCount);
    }

    public void fixRating(Double oldRating, Double newRating) {
        // 이전 총 별점수와 별점을 준 횟수를 가져옵니다.
        double oldTotalRating = getTotalRating();
        double oldRatingCount = getRatingCount();

        // 새로운 총 별점수와 별점을 준 횟수를 계산합니다.
        double newTotalRating = oldTotalRating - oldRating + newRating;

        // 총 별점수와 별점을 준 횟수를 업데이트합니다.
        setTotalRating(newTotalRating);
        setRating(newTotalRating / oldRatingCount);
    }

    public void revertRating(Double oldRating) {
        // 이전 총 별점수와 별점을 준 횟수를 가져옵니다.
        double oldTotalRating = getTotalRating();
        double oldRatingCount = getRatingCount();

        // 새로운 총 별점수와 별점을 준 횟수를 계산합니다.
        double newTotalRating = oldTotalRating - oldRating;
        double newRatingCount = oldRatingCount - 1;

        // 총 별점수와 별점을 준 횟수를 업데이트합니다.
        setTotalRating(newTotalRating);
        setRatingCount(newRatingCount);
        setRating(newTotalRating / newRatingCount);
    }
}

