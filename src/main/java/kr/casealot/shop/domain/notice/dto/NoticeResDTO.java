package kr.casealot.shop.domain.notice.dto;

import kr.casealot.shop.domain.customer.entity.Customer;
import kr.casealot.shop.domain.notice.comment.entity.NoticeComment;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class NoticeResDTO {
    private Long id;
    private String customerId;
    private String title;
    private String content;
    private String photoUrl;
    private int views;
    private List<NoticeComment> noticeCommentList;
}
