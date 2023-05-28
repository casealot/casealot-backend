package kr.casealot.shop.domain.product.dto;

import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class SortDTO {
    private String field;
    /**
     * DESC
     *
     */
    private String option;
}
