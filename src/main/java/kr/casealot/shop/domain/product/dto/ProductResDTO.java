package kr.casealot.shop.domain.product.dto;

import kr.casealot.shop.domain.product.review.dto.ReviewResDTO;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductResDTO {
    private String name;
    private String content;
    private int price;
    private int sale;
    private int views;
    private String color;
    private String season;
    private String type;

    private List<ReviewResDTO> reviewList;
}
