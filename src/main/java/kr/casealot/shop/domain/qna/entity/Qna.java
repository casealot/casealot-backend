package kr.casealot.shop.domain.qna.entity;

import kr.casealot.shop.domain.qna.comment.entity.QnaComment;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "qna")
@Builder
public class Qna {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String content;
    private String photoUrl;
    private LocalDateTime registrationDate;
    private LocalDateTime modificationDate;

    @Builder.Default
    @OneToMany(mappedBy = "qna", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<QnaComment> qnaCommentList = new ArrayList<>();
}
