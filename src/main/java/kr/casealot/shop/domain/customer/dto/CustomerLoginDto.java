package kr.casealot.shop.domain.customer.dto;

import kr.casealot.shop.global.oauth.entity.RoleType;
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