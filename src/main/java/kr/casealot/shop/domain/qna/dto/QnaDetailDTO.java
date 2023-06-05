package kr.casealot.shop.domain.qna.dto;

import kr.casealot.shop.domain.qna.comment.dto.QnaCommentResDTO;
import lombok.*;

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
    private List<QnaCommentResDTO> qnaCommentList;
}
