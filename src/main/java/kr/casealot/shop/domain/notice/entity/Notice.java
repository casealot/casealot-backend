package kr.casealot.shop.domain.notice.entity;

import kr.casealot.shop.domain.customer.entity.Customer;
import kr.casealot.shop.domain.notice.comment.entity.NoticeComment;
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
@Entity(name = "notice")
@Builder
public class Notice extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String content;
    private int views;

    @Builder.Default
    @OneToMany(mappedBy = "notice", cascade = CascadeType.ALL)
    private List<NoticeComment> noticeCommentList = new ArrayList<>();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "CUSTOMER_SEQ")
    private Customer customer;
}
