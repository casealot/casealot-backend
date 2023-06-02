package kr.casealot.shop.domain.product.dto;

import kr.casealot.shop.domain.product.review.dto.ReviewResDTO;
import kr.casealot.shop.domain.product.review.entity.Review;
import kr.casealot.shop.domain.product.review.reviewcomment.entity.ReviewComment;
import lombok.*;

import javax.persistence.Column;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductGetDTO {
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

    private List<ReviewResDTO> reviewList;
}
