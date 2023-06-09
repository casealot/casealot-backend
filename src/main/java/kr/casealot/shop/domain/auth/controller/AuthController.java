package kr.casealot.shop.domain.auth.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.casealot.shop.domain.auth.entity.CustomerRefreshToken;
import kr.casealot.shop.domain.auth.repository.CustomerRefreshTokenRepository;
import kr.casealot.shop.domain.customer.dto.CustomerTokenDto;
import kr.casealot.shop.domain.customer.entity.Customer;
import kr.casealot.shop.domain.customer.repository.CustomerRepository;
import kr.casealot.shop.global.common.APIResponse;
import kr.casealot.shop.global.config.properties.AppProperties;
import kr.casealot.shop.domain.auth.exception.NoRefreshTokenException;
import kr.casealot.shop.domain.auth.exception.NotFoundRefreshTokenException;
import kr.casealot.shop.global.exception.NotFoundUserException;
import kr.casealot.shop.global.oauth.entity.RoleType;
import kr.casealot.shop.global.oauth.token.AuthToken;
import kr.casealot.shop.global.oauth.token.AuthTokenProvider;
import kr.casealot.shop.global.util.HeaderUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/cal/v1/auth")
@Api(tags = {"AUTH API"}, description = "권한 관련 API")
@RequiredArgsConstructor
public class AuthController {

    private final AppProperties appProperties;
    private final AuthTokenProvider tokenProvider;
    private final AuthenticationManager authenticationManager;
    private final CustomerRefreshTokenRepository customerRefreshTokenRepository;
    private final CustomerRepository customerRepository;

    private final static long THREE_DAYS_MSEC = 259200000;
    private final static String REFRESH_TOKEN = "refresh_token";

    @GetMapping("/refresh")
    @ApiOperation(value = "토큰 재발급", notes = "리프레시 토큰을 통해 JWT 토큰을 재발급한다.")
    public APIResponse<CustomerTokenDto> refreshToken (HttpServletRequest request) throws Exception {
        // refresh token
        String refreshToken = HeaderUtil.getRefreshToken(request);
        if(null == refreshToken){
            throw new NoRefreshTokenException();
        }

        log.info("refreshToken ====> {}", refreshToken);

        AuthToken authRefreshToken = tokenProvider.convertAuthToken(refreshToken);

        // userId refresh token으로 DB 확인
        CustomerRefreshToken customerRefreshToken = customerRefreshTokenRepository.findByRefreshToken(refreshToken);

        if(customerRefreshToken == null){
            throw new NotFoundRefreshTokenException();
        }

        String userId = customerRefreshToken.getId();
        if(userId == null){
            throw new NotFoundUserException();
        }
        Customer customer = customerRepository.findById(userId);
        RoleType roleType = customer.getRoleType();

        Date now = new Date();
        AuthToken newAccessToken = tokenProvider.createAuthToken(
            userId,
            roleType.getCode(),
            new Date(now.getTime() + appProperties.getAuth().getTokenExpiry())
        );

        long validTime = authRefreshToken.getTokenClaims().getExpiration().getTime() - now.getTime();

        // refresh 토큰 기간이 3일 이하로 남은 경우, refresh 토큰 갱신
        if (validTime <= THREE_DAYS_MSEC) {
            // refresh 토큰 설정
            long refreshTokenExpiry = appProperties.getAuth().getRefreshTokenExpiry();

            authRefreshToken = tokenProvider.createAuthToken(
                appProperties.getAuth().getTokenSecret(),
                new Date(now.getTime() + refreshTokenExpiry)
            );

            // DB에 refresh 토큰 업데이트
            customerRefreshToken.setRefreshToken(authRefreshToken.getToken());
            customerRefreshTokenRepository.save(customerRefreshToken);
        }
        CustomerTokenDto customerTokenDto = CustomerTokenDto.builder()
            .customerId(customerRefreshToken.getId())
            .accessToken(newAccessToken.getToken())
            .roleType(customer.getRoleType())
            .refreshToken(customerRefreshToken.getRefreshToken()).build();

        return APIResponse.success(customerTokenDto);
    }

}