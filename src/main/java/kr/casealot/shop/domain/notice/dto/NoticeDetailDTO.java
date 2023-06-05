package kr.casealot.shop.domain.notice.dto;

import kr.casealot.shop.domain.notice.comment.dto.NoticeCommentResDTO;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NoticeDetailDTO {
    private Long id;
    private String customerId;
    private String title;
    private String content;
    private int views;
    private List<NoticeCommentResDTO> noticeCommentList;
}