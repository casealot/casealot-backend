package com.cal.casealotbackend.domain.product.list;

import com.cal.casealotbackend.domain.BaseEntity;

import javax.persistence.Entity;

@Entity(name = "wish_list")
public class WishList extends BaseEntity {
    private Long user_id;

}
