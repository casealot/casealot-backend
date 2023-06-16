package kr.casealot.shop.domain.customer.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDto {
    private String id;
    private String name;
    private String password;
    private String phoneNumber;
    private String email;
    private String profileImageUrl;
    private String address;
    private String addressDetail;
}