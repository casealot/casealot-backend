package kr.casealot.shop.domain.notice.dto;

import kr.casealot.shop.domain.notice.comment.dto.NoticeCommentResDTO;
import kr.casealot.shop.domain.notice.comment.entity.NoticeComment;
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
    private String photoUrl;
    private int views;
    private List<NoticeCommentResDTO> noticeCommentList;
}
