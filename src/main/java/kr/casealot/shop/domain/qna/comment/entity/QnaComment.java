package kr.casealot.shop.domain.qna.comment.entity;

import kr.casealot.shop.domain.qna.entity.Qna;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "qna_comment")
@Builder
public class QnaComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "qna_id")
    private Qna qna;

    private String content;

    private LocalDateTime registrationDate;
    private LocalDateTime modificationDate;
}
