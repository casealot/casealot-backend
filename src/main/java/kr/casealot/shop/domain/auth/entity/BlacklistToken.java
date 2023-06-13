package kr.casealot.shop.domain.auth.entity;

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
@Table(name = "CUSTOMER_BLACKLIST_TOKEN")
public class BlacklistToken {
    @Id
    @Column(name = "BLACKLIST_TOKEN_SEQ")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long blacklistTokenSeq;

    @Column(name = "CUSTOMER_ID", length = 64)
    @NotNull
    @Size(max = 64)
    private String id;

    @Column(name = "BLACKLIST_TOKEN", length = 256)
    @NotNull
    @Size(max = 256)
    private String blacklistToken;
}