package kr.casealot.shop.domain.product.dto;


import kr.casealot.shop.domain.product.entity.Product;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class ProductResDTO {
    private List<Product> items = new ArrayList<>();
    private Long count;
    private Long totalPages;
    private Long totalCount;

    @Builder
    public ProductResDTO(List<Product> items, Long count , Long totalPages, Long totalCount){
        this.items= items;
        this.count= count;
        this.totalPages= totalPages;
        this.totalCount= totalCount;
    }
}
