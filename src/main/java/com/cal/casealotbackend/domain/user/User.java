package com.cal.casealotbackend.domain.user;


import com.cal.casealotbackend.domain.BaseEntity;

import javax.persistence.Entity;

@Entity(name = "user")
public class User extends BaseEntity {

    private String userId;
    private String password;
    private String userName;
    private String phoneNumber;
    private String address; //카카오 API 사용해야함
    private String jwtToken;
    private String orderList;
    private String cart;
    private String wishList;
    private String role;
}
