package com.cal.casealotbackend.domain.board.QnA;

import com.cal.casealotbackend.domain.BaseEntity;

import javax.persistence.Entity;

@Entity(name = "qna_comment")
public class QnAComment extends BaseEntity {
    private Long qnaId;
    private Long userId;
    private String content;
}
