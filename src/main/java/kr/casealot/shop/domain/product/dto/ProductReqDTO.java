package kr.casealot.shop.domain.product.dto;

import lombok.Data;
import lombok.Getter;

import java.util.List;

@Data
@Getter
public class ProductReqDTO {
    private String query;
    private List<SearchFilter> filter;
    private int size;
    private int page;

    private List<SortDTO> sort;

}
