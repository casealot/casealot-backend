package kr.casealot.shop.domain.notice.comment.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NoticeCommentDetailDTO {
    private Long id;
    private String customerId;
    private String title;
    private String content;
}