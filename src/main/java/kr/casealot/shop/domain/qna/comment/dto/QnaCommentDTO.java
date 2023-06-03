package kr.casealot.shop.domain.qna.comment.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QnaCommentDTO {
    private Long id;
    private String customerId;
    private String title;
    private String content;
}
