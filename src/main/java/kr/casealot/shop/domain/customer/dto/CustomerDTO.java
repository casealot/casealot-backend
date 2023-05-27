package kr.casealot.shop.domain.customer.dto;

import kr.casealot.shop.global.oauth.entity.RoleType;
import lombok.Data;

@Data
public class CustomerDTO {
    private String id;
    private String name;
    private String password;
    private String email;
    private String profileImageUrl;
    private String postNo;
    private String address;
    private String addressDetail;
    private RoleType roleType;
}
