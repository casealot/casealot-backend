package kr.casealot.shop.domain.notice.comment.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NoticeCommentResDTO {
    private Long id;
    private String customerId;
    private String title;
    private String content;
}
