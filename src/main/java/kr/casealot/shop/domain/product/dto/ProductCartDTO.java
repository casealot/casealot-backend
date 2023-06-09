package kr.casealot.shop.domain.product.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductCartDTO {
    private Long id;
    private String name;
    private int price;
    private int quantity;
    private double calculatePrice;
    private String thumbnail ="";
    private String content;
    private String color;
    private String season;
    private String type;
}
