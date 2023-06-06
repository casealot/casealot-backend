package kr.casealot.shop.domain.auth.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RefreshTokenReqDTO {
    private String refreshToken;
}
