package kr.casealot.shop.domain.customer.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import kr.casealot.shop.oauth.entity.ProviderType;
import kr.casealot.shop.oauth.entity.RoleType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "CUSTOMER")
public class Customer {
    @JsonIgnore
    @Id
    @Column(name = "CUSTOMER_SEQ")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long customerSeq;

    @Column(name = "CUSTOMER_ID", length = 64, unique = true)
    private String customerId;

    @Column(name = "CUSTOMER_NAME", length = 100)
    private String customerName;

    @JsonIgnore
    @Column(name = "PASSWORD", length = 128)
    private String password;

    @Column(name = "EMAIL", length = 512, unique = true)
    private String email;

    @Column(name = "EMAIL_VERIFIED_YN", length = 1)
    private String emailVerifiedYn;

//    @Column(name = "PROFILE_IMAGE_URL", length = 512)
//    private String profileImageUrl;

    @Column(name = "PROVIDER_TYPE", length = 20)
    @Enumerated(EnumType.STRING)
    private ProviderType providerType;

    @Column(name = "ROLE_TYPE", length = 20)
    @Enumerated(EnumType.STRING)
    private RoleType roleType;

    @Column(name = "CREATED_AT")
    private LocalDateTime createdAt;

    @Column(name = "MODIFIED_AT")
    private LocalDateTime modifiedAt;

    public Customer(
            String userId,
            String username,
            String email,
            String emailVerifiedYn,
            //String profileImageUrl,
            ProviderType providerType,
            RoleType roleType,
            LocalDateTime createdAt,
            LocalDateTime modifiedAt
    ) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.password = "NO_PASS";
        this.email = email != null ? email : "NO_EMAIL";
        this.emailVerifiedYn = emailVerifiedYn;
        //this.profileImageUrl = profileImageUrl != null ? profileImageUrl : "";
        this.providerType = providerType;
        this.roleType = roleType;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }
}