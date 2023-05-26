package kr.casealot.shop.domain.qna.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import kr.casealot.shop.domain.qna.comment.entity.QnaComment;
import kr.casealot.shop.domain.qna.entity.Qna;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QnaDTO {
    private Long id;
    private String title;
    private String content;
    private String photoUrl;
    private int views;
    private LocalDateTime registrationDate;
    private LocalDateTime modificationDate;
}
