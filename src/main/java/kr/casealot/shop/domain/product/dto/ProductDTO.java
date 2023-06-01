package kr.casealot.shop.domain.product.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;

@Getter
@Setter
public class ProductDTO {
    private Long id;
    private Long userId;
    private String name;
    private String content;

    private int price;
    private int views;

    private String img_B;
    private String img_M;
    private String img_S;

    private int sells;
    private int sale;

    private String color;
    private String season;
    private String type;
}
