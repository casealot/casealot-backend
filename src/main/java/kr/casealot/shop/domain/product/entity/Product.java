package kr.casealot.shop.domain.product.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import kr.casealot.shop.domain.customer.entity.Customer;
import kr.casealot.shop.domain.file.entity.UploadFile;
import kr.casealot.shop.domain.product.review.entity.Review;
import kr.casealot.shop.domain.wishlist.wishlistItem.entity.WishlistItem;
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
@Builder
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

    //상품 1개에 리뷰 n개 (1:n 설정)
    @JsonBackReference
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<Review> reviews = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<WishlistItem> wishlistItemList = new ArrayList<>();

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
}
