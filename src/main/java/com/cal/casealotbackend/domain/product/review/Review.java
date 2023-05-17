package com.cal.casealotbackend.domain.product.review;

import com.cal.casealotbackend.domain.BaseEntity;

import javax.persistence.Entity;

@Entity(name = "review")
public class Review extends BaseEntity {
    private Long productId;
    private String title;
    private String content;
}
