package kr.casealot.shop.domain.customer.dto;

import kr.casealot.shop.global.oauth.entity.RoleType;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerTokenDto { //로그인시 response
    private String customerId;
    private String accessToken;
    private String refreshToken;
    private RoleType roleType;
}
