package com.cal.casealotbackend.domain.board.Notice;

import com.cal.casealotbackend.domain.BaseEntity;

import javax.persistence.Entity;

@Entity(name = "notice_comment")
public class NoticeComment extends BaseEntity {
    private Long noticeId;
    private Long userId;
    private String content;
}
