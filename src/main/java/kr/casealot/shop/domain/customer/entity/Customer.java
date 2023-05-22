package kr.casealot.shop.domain.customer.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import kr.casealot.shop.global.entity.BaseTimeEntity;
import kr.casealot.shop.global.oauth.entity.ProviderType;
import kr.casealot.shop.global.oauth.entity.RoleType;
import lombok.*;

import javax.persistence.*;

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

    @JsonIgnore
    @Column(name = "PASSWORD", length = 128)
    private String password;

    @Column(name = "EMAIL", length = 512, unique = true)
    private String email;

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

    public Customer(
            String id,
            String name,
            String email,
            String emailVerifiedYn,
            String profileImageUrl,
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
        this.roleType = roleType;
    }
}