package com.cal.casealotbackend.domain.product.list;

import com.cal.casealotbackend.domain.BaseEntity;

import javax.persistence.Entity;

@Entity(name = "order_item")
public class OrderItem extends BaseEntity {
    private Long user_id;
}
