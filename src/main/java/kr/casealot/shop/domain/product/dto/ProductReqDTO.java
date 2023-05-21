package kr.casealot.shop.domain.product.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
public class ProductReqDTO {
    private String filter;
}
