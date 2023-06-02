package kr.casealot.shop.domain.notice.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
public class NoticeReqDTO {
    private String title;
    private String content;
    private String photoUrl;
}
