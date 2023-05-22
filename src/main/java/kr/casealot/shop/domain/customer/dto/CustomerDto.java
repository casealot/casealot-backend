package kr.casealot.shop.domain.customer.dto;

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
}
