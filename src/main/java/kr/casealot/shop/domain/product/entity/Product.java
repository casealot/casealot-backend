package kr.casealot.shop.domain.product.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
public class Product {
    @Id
    @GeneratedValue
    @Column(name = "PRODUCT_ID")
    private Long id;

    @Column(name = "PRODUCT_NAME", length = 1024)
    private String name;

    @Column(name = "PRODUCT_CONTENT", length = 4000)
    private String content;

    @Column(name = "PRODUCT_COLOR", length = 50)
    private String color;

}
