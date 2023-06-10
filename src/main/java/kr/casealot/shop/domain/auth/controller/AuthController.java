package kr.casealot.shop.domain.auth.controller;

import io.jsonwebtoken.Claims;
import io.swagger.annotations.ApiOperation;
import kr.casealot.shop.domain.auth.entity.CustomerRefreshToken;
import kr.casealot.shop.domain.auth.repository.CustomerRefreshTokenRepository;
import kr.casealot.shop.domain.customer.entity.Customer;
import kr.casealot.shop.domain.customer.repository.CustomerRepository;
import kr.casealot.shop.domain.customer.service.CustomerService;
import kr.casealot.shop.global.common.APIResponse;
import kr.casealot.shop.global.config.properties.AppProperties;
import kr.casealot.shop.global.exception.NotFoundException;
import kr.casealot.shop.global.oauth.entity.RoleType;
import kr.casealot.shop.global.oauth.token.AuthToken;
import kr.casealot.shop.global.oauth.token.AuthTokenProvider;
import kr.casealot.shop.global.util.CookieUtil;
import kr.casealot.shop.global.util.HeaderUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Slf4j
@RestController
@RequestMapping("/cal/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AppProperties appProperties;
    private final AuthTokenProvider tokenProvider;
    private final AuthenticationManager authenticationManager;
    private final CustomerRefreshTokenRepository customerRefreshTokenRepository;
    private final CustomerRepository customerRepository;

    private final static long THREE_DAYS_MSEC = 259200000;
    private final static String REFRESH_TOKEN = "refreshToken";

    @ApiOperation(value = "토큰 재발급")
    @GetMapping("/refresh")
    public APIResponse refreshToken (HttpServletRequest request) throws Exception {
        // refresh token cookie에서 갖고옴

        // refresh token
        String refreshToken = CookieUtil.getCookie(request, REFRESH_TOKEN)
                .map(Cookie::getValue)
                .orElseThrow(() -> new NotFoundException("Not Found RefreshToken From Cookie"));

        AuthToken authRefreshToken = tokenProvider.convertAuthToken(refreshToken);

        // userId refresh token으로 DB 확인
        CustomerRefreshToken customerRefreshToken = customerRefreshTokenRepository.findByRefreshToken(refreshToken);

        String userId = customerRefreshToken.getId();
        Customer customer = customerRepository.findById(userId);
        RoleType roleType = customer.getRoleType();

        if (customerRefreshToken == null) {
            return APIResponse.invalidRefreshToken();
        }

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

        return APIResponse.success("token", newAccessToken.getToken());
    }

}