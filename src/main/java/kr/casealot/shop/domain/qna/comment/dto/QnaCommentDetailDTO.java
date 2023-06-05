package kr.casealot.shop.domain.qna.comment.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QnaCommentDetailDTO {
    private Long id;
    private String customerId;
    private String title;
    private String content;
}