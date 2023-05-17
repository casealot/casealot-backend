package com.cal.casealotbackend.domain.product;


import com.cal.casealotbackend.domain.BaseEntity;

import javax.persistence.Entity;

@Entity(name = "category")
public class Category extends BaseEntity {
    private String name;
}
