package com.cal.casealotbackend.domain.board.QnA;

import com.cal.casealotbackend.domain.BaseEntity;

import javax.persistence.Entity;

@Entity(name = "qna")
public class QnA extends BaseEntity {
    private Long userId;
    private String title;
    private String content;
}
