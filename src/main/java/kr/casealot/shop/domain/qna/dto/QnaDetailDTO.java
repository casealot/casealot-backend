package kr.casealot.shop.domain.qna.dto;

import kr.casealot.shop.domain.qna.comment.dto.QnaCommentDTO;
import kr.casealot.shop.domain.qna.comment.entity.QnaComment;
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
    private String photoUrl;
    private int views;
    private List<QnaCommentDTO> qnaCommentList;
}
