package kr.casealot.shop.domain.qna.comment.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QnaCommentReqDTO {
    private String title;
    private String content;
}