package com.cal.casealotbackend.domain.product.list;

import com.cal.casealotbackend.domain.BaseEntity;

import javax.persistence.Entity;

@Entity(name = "cart_list")
public class CartList extends BaseEntity {
    private Long user_id;

}
