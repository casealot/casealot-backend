package kr.casealot.shop.domain.customer.dto;

import kr.casealot.shop.global.oauth.entity.RoleType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CustomerDto {
    private String id;
    private String name;
    private String password;
    private String email;
    private String profileImageUrl;
    private String address;
    private String addressDetail;
    private RoleType roleType;
}
