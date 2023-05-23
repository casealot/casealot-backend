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
    private Long qnaId;
    private String title;
    private String content;
    private LocalDateTime registrationDate;
    private LocalDateTime modificationDate;
}
