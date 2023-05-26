package kr.casealot.shop.domain.qna.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import kr.casealot.shop.domain.qna.comment.entity.QnaComment;
import lombok.*;
import net.bytebuddy.implementation.bind.annotation.Default;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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
    private int views;
    private LocalDateTime registrationDate;
    private LocalDateTime modificationDate;

    @Builder.Default
    @OneToMany(mappedBy = "qna", cascade = CascadeType.ALL)
    private List<QnaComment> qnaCommentList = new ArrayList<>();
}
