package com.cal.casealotbackend.domain.product;


import com.cal.casealotbackend.domain.BaseEntity;

import javax.persistence.Entity;

@Entity(name = "product")
public class Product extends BaseEntity {
    private String categoryId;
    private String name;
    private String info;
    private Long price;
    private Double discount;
//    private List<Product> wishlist; //더 생각해볼 것
    private Double starAvg; //별점
    private String viewCnt;
    private String saleCnt;
    private String stock; //재고
}
