package kr.casealot.shop.domain.notice.comment.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import kr.casealot.shop.domain.customer.entity.Customer;
import kr.casealot.shop.domain.notice.entity.Notice;
import kr.casealot.shop.global.entity.BaseTimeEntity;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "notice_comment")
@Builder
public class NoticeComment extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "notice_id")
    private Notice notice;

    private String title;
    private String content;


    @JsonManagedReference
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "CUSTOMER_SEQ")
    private Customer customer;

}