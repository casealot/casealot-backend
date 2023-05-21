package com.cal.casealotbackend.domain.user.domain;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private String password;
    private String userName;
    private String phoneNumber;
    private String address; //카카오 API 사용해야함

    //    private String jwtToken; //토큰이 db에 저장이 되어야하는가? (난 저장하지않았음)

    //    private List<order> orderListId; //order만들어야함
    //    private List<cart> cartId; //cart 만들어야함
    //    private List<wishlist> wishListId; //wishlist만들어야함
}
