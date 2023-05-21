package com.cal.casealotbackend.domain.user.domain;


import com.cal.casealotbackend.domain.BaseEntity;
import com.cal.casealotbackend.domain.user.type.UserType;
import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Entity(name = "user")
@Getter
public class User extends BaseEntity {

    //id, createdAt, updatedAt 은 BaseEntity 에 있음
    private String password;
    private String userName;
    private String phoneNumber;
    private String address; //카카오 API 사용해야함
    //    private String jwtToken; //토큰이 db에 저장이 되어야하는가? (난 저장하지않았음)
    //    private List<order> orderListId; //order만들어야함
    //    private List<cart> cartId; //cart 만들어야함
    //    private List<wishlist> wishListId; //wishlist만들어야함

    @Enumerated(EnumType.STRING)
    private UserType role = UserType.DEFAULT; //기본 지정 USER
}
