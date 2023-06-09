package kr.casealot.shop.domain.customer.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import kr.casealot.shop.domain.cart.entity.Cart;
import kr.casealot.shop.domain.file.entity.UploadFile;
import kr.casealot.shop.domain.notice.comment.entity.NoticeComment;
import kr.casealot.shop.domain.notice.entity.Notice;
import kr.casealot.shop.domain.order.entity.Order;
import kr.casealot.shop.domain.product.entity.Product;
import kr.casealot.shop.domain.product.review.entity.Review;
import kr.casealot.shop.domain.product.review.reviewcomment.entity.ReviewComment;
import kr.casealot.shop.domain.qna.comment.entity.QnaComment;
import kr.casealot.shop.domain.qna.entity.Qna;
import kr.casealot.shop.domain.wishlist.entity.Wishlist;
import kr.casealot.shop.global.entity.BaseTimeEntity;
import kr.casealot.shop.global.oauth.entity.ProviderType;
import kr.casealot.shop.global.oauth.entity.RoleType;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "CUSTOMER")
public class Customer extends BaseTimeEntity {
    @JsonIgnore
    @Id
    @Column(name = "CUSTOMER_SEQ")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @Column(name = "CUSTOMER_ID", length = 64, unique = true)
    private String id;

    @Column(name = "CUSTOMER_NAME", length = 100)
    private String name;

    @Column(name = "PASSWORD", length = 128)
    private String password;

    @Column(name = "PHONE_NUMBER", length = 11)
    private String phoneNumber;

    @Column(name = "EMAIL", length = 512)
    private String email;

    @JsonIgnore
    @Column(name = "EMAIL_VERIFIED_YN", length = 1)
    private String emailVerifiedYn;

    @Column(name = "PROFILE_IMAGE_URL", length = 512)
    private String profileImageUrl;

    @Column(name = "PROVIDER_TYPE", length = 20)
    @Enumerated(EnumType.STRING)
    private ProviderType providerType;

    @Column(name = "ROLE_TYPE", length = 20)
    @Enumerated(EnumType.STRING)
    private RoleType roleType;

    @Column(name = "ADDRESS", length = 512)
    private String address;

    @Column(name = "ADDRESS_DETAIL", length = 128)
    private String addressDetail;

    // 프로필 사진
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private UploadFile profileImg;

    @JsonBackReference
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private List<Qna> qnaList;

    @JsonBackReference
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private List<QnaComment> qnaCommentList;

    @JsonBackReference
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private List<Notice> noticeList;

    @JsonBackReference
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private List<NoticeComment> noticeCommentList;

    @JsonBackReference
    @OneToOne(mappedBy = "customer", cascade = CascadeType.ALL)
    private Cart cartList;

    //사용자가 사라져도, 리뷰는 탈퇴한 회원입니다. 를 남기기 위함
    @JsonBackReference
    @OneToMany(mappedBy = "customer")
    private List<Review> reviewList;

    @JsonBackReference
    @OneToMany(mappedBy = "customer")
    private List<ReviewComment> reviewCommentList;

    @JsonBackReference
    @OneToMany(mappedBy = "customer")
    private List<Product> productList;

    @JsonBackReference
    @OneToOne(mappedBy = "customer", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Wishlist wishList;

    @JsonBackReference
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private List<Order> orders = new ArrayList<>();

    /**
     * Create Customer for OAuth
     *
     * @param id
     * @param name
     * @param email
     * @param emailVerifiedYn
     * @param providerType
     * @param roleType
     */
    public Customer(
            String id,
            String name,
            String email,
            String emailVerifiedYn,
            String profileImageUrl,
            String phoneNumber,
            ProviderType providerType,
            RoleType roleType
    ) {
        this.id = id;
        this.name = name;
        this.password = "NO_PASS";
        this.email = email != null ? email : "NO_EMAIL";
        this.emailVerifiedYn = emailVerifiedYn;
        this.profileImageUrl = profileImageUrl != null ? profileImageUrl : "";
        this.providerType = providerType;
        this.phoneNumber = phoneNumber;
        this.roleType = roleType;
    }

    @Builder
    public Customer(
            String id,
            String name,
            String password,
            String phoneNumber,
            String email,
            String emailVerifiedYn,
            String profileImageUrl,
            ProviderType providerType,
            RoleType roleType,
            String address,
            String addressDetail,
            List<Review> reviewList
    ) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.email = email != null ? email : "NO_EMAIL";
        this.emailVerifiedYn = emailVerifiedYn;
        this.profileImageUrl = profileImageUrl != null ? profileImageUrl : "";
        this.providerType = providerType;
        this.roleType = roleType;
        this.address = address;
        this.addressDetail = addressDetail;
        // ? 이렇게 하는게 맞나
        this.reviewList = reviewList;
    }

    public Customer(String id, String password) {
        this.id=id;
        this.password=password;
    }
}