package kr.casealot.shop.domain.product.dto;

import kr.casealot.shop.domain.file.entity.UploadFile;
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
    private String thumbnail ="";
    private String content;
    private String color;
    private String season;
    private String type;
}
