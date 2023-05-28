package kr.casealot.shop.domain.product.dto;

import lombok.Data;
import lombok.Getter;
import org.springframework.core.annotation.Order;
import org.springframework.data.domain.Sort;

import java.util.List;

@Data
@Getter
public class ProductReqDTO {
    private String query;
    private int size;
    private int page;

    private List<SortDTO> sort;

}
