package kr.casealot.shop.domain.notice.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


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
}
