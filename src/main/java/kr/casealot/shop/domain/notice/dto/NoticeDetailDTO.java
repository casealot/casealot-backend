package kr.casealot.shop.domain.notice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import kr.casealot.shop.domain.notice.comment.dto.NoticeCommentResDTO;
import lombok.*;

import java.time.LocalDateTime;
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
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdDt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime modifiedDt;
}