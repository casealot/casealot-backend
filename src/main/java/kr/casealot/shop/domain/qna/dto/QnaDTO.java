package kr.casealot.shop.domain.qna.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import kr.casealot.shop.domain.qna.comment.entity.QnaComment;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QnaDTO {
    private String title;
    private String content;
    private String photoUrl;
    private LocalDateTime registrationDate;
    private LocalDateTime modificationDate;
    private List<QnaComment> qnaCommentList;
}
