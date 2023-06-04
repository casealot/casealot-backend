package kr.casealot.shop.domain.auth.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import kr.casealot.shop.domain.customer.entity.Customer;
import kr.casealot.shop.global.entity.BaseTimeEntity;
import kr.casealot.shop.global.oauth.entity.RoleType;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "CustomerToken")
public class CustomerToken {

    @JsonIgnore
    @Id
    @Column(name = "REFRESH_TOKEN_SEQ")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long refreshTokenSeq;

    @Column(name = "CUSTOMER_SEQ", length = 64, unique = true)
    @NotNull
    private Long customerSeq = 0L;

    @Column(name = "JWT_TOKEN", length = 256)
    @NotNull
    @Size(max = 256)
    private String jwtToken;

    @Column(name = "REFRESH_TOKEN", length = 256)
    @NotNull
    @Size(max = 256)
    private String refreshToken;

    @Column(name = "ROLE_TYPE", length = 256)
    @NotNull
    private RoleType roleType;

    public CustomerToken(Long customerSeq, String jwtToken, String refreshToken, RoleType roleType) {
        this.customerSeq = customerSeq;
        this.jwtToken = jwtToken;
        this.refreshToken = refreshToken;
        this.roleType = roleType;
    }
}
