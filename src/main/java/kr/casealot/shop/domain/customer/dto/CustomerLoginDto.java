package kr.casealot.shop.domain.customer.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerLoginDto {
    private String id;
    private String password;
}