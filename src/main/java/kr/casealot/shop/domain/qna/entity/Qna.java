package kr.casealot.shop.domain.qna.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import kr.casealot.shop.domain.customer.entity.Customer;
import kr.casealot.shop.domain.qna.comment.entity.QnaComment;
import kr.casealot.shop.global.entity.BaseTimeEntity;
import lombok.*;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;





@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "qna")
@Builder
public class Qna extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String content;
    private int views;

    @OneToMany(mappedBy = "qna", cascade = CascadeType.ALL)
    private List<QnaComment> qnaCommentList;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "CUSTOMER_SEQ")
    private Customer customer;
}
