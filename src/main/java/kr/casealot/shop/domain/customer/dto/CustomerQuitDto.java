package kr.casealot.shop.domain.customer.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CustomerQuitDto {
    private String id;
    private String name;
    private String password;
}