package com.cal.casealotbackend.domain.product.review;

import com.cal.casealotbackend.domain.BaseEntity;

import javax.persistence.Entity;

@Entity(name = "review_comment")
public class ReviewComment extends BaseEntity {
    private Long reviewId;
    private String content;
}
