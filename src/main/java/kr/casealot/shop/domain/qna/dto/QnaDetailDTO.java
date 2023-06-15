package kr.casealot.shop.domain.qna.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import kr.casealot.shop.domain.qna.comment.dto.QnaCommentResDTO;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QnaDetailDTO {
    private Long id;
    private String customerId;
    private String title;
    private String content;
    private int views;
    private String available; // 수정 삭제 가능 여부

    private List<QnaCommentResDTO> qnaCommentList;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdDt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime modifiedDt;
}
