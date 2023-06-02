package kr.casealot.shop.domain.notice.comment.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class NoticeCommentResDTO {
    private Long id;
    private String customerId;
    private String title;
    private String content;
}
