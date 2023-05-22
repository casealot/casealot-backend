package kr.casealot.shop.domain.product.entity;

import kr.casealot.shop.global.entity.BaseTimeEntity;
import lombok.*;
import net.minidev.json.annotate.JsonIgnore;

import javax.persistence.*;

/**
 * TODO DB 설계 대로 entity 작성
 */
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

    /**
     * TODO : review 조인
     */

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
    /**
     * NEW
     * BEST ..etc
     */
    @Column(name = "PRODUCT_TYPE", length = 50)
    private String type;

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
