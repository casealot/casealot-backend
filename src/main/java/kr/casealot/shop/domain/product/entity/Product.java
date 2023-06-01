package kr.casealot.shop.domain.product.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import kr.casealot.shop.domain.customer.entity.Customer;
import kr.casealot.shop.domain.product.review.entity.Review;
import kr.casealot.shop.global.entity.BaseTimeEntity;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "PRODUCT")
public class Product extends BaseTimeEntity {
    @Id
    @GeneratedValue
    @Column(name = "PRODUCT_ID")
    private Long id;

    @JsonBackReference
    @ManyToOne
    private Customer customer;

    @Column(name = "PRODUCT_NAME", length = 1024)
    private String name;

    @Column(name = "PRODUCT_CONTENT", length = 4000)
    private String content;

    @Column(name = "PRODUCT_IMG_B", length = 512)
    private String img_B;

    @Column(name = "PRODUCT_IMG_M", length = 512)
    private String img_M;

    @Column(name = "PRODUCT_IMG_S", length = 512)
    private String img_S;

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

    //상품 1개에 리뷰 n개 (1:n 설정)
    @JsonBackReference
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<Review> reviews = new ArrayList<>();

    @Builder
    public Product(String name, String content, int price, int views, int sells, String color, String season) {
        this.name = name;
        this.content = content;
        this.price = price;
        this.views = views;
        this.sells = sells;
        this.color = color;
        this.season = season;
    }
}
