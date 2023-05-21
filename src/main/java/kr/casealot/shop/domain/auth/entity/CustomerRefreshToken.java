package kr.casealot.shop.domain.auth.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import kr.casealot.shop.global.entity.BaseTimeEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "CUSTOMER_REFRESH_TOKEN")
public class CustomerRefreshToken extends BaseTimeEntity{

    @JsonIgnore
    @Id
    @Column(name = "REFRESH_TOKEN_SEQ")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long refreshTokenSeq;

    @Column(name = "CUSTOMER_ID", length = 64, unique = true)
    @NotNull
    @Size(max = 64)
    private String id;

    @Column(name = "REFRESH_TOKEN", length = 256)
    @NotNull
    @Size(max = 256)
    private String refreshToken;

    public CustomerRefreshToken(
            @NotNull @Size(max = 64) String id,
            @NotNull @Size(max = 256) String refreshToken
    ) {
        this.id = id;
        this.refreshToken = refreshToken;
    }
}
