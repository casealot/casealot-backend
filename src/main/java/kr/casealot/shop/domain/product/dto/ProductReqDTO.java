package kr.casealot.shop.domain.product.dto;

import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class ProductReqDTO {
    private String query;
    private int size;
    private int page;

    private SortDTO sort;

}
