package kr.casealot.shop.domain.product.dto;

import lombok.Data;

@Data
public class SearchFilter {
    private String key;
    private String operation;
    private String value;

}
