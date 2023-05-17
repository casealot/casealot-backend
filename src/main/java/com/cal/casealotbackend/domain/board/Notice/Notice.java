package com.cal.casealotbackend.domain.board.Notice;

import com.cal.casealotbackend.domain.BaseEntity;

import javax.persistence.Entity;

@Entity(name = "notice")
public class Notice extends BaseEntity {
    private Long userId;
    private String title;
    private String content;
}
