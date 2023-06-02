package kr.casealot.shop.domain.notice.comment.dto;

import lombok.*;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NoticeCommentDTO {
    private Long id;
    private String customerId;
    private Long qnaId;
    private String title;
    private String content;
}

