package com.cal.casealotbackend.domain.product.list;

import com.cal.casealotbackend.domain.BaseEntity;

import javax.persistence.Entity;

@Entity(name = "wish_item")
public class WishItem extends BaseEntity {
    private Long user_id;

}
