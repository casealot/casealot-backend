package kr.casealot.shop.domain.auth.controller;

import io.jsonwebtoken.Claims;
import kr.casealot.shop.domain.auth.dto.RefreshTokenReqDTO;
import kr.casealot.shop.domain.auth.entity.CustomerRefreshToken;
import kr.casealot.shop.domain.auth.repository.CustomerRefreshTokenRepository;
import kr.casealot.shop.domain.customer.service.CustomerService;
import kr.casealot.shop.global.common.APIResponse;
import kr.casealot.shop.global.config.properties.AppProperties;
import kr.casealot.shop.global.oauth.entity.RoleType;
import kr.casealot.shop.global.oauth.token.AuthToken;
import kr.casealot.shop.global.oauth.token.AuthTokenProvider;
import kr.casealot.shop.global.util.CookieUtil;
import kr.casealot.shop.global.util.HeaderUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

@RestController
@RequestMapping("/cal/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AppProperties appProperties;
    private final AuthTokenProvider tokenProvider;
    private final AuthenticationManager authenticationManager;
    private final CustomerRefreshTokenRepository customerRefreshTokenRepository;
    private final CustomerService customerService;

    private final static long THREE_DAYS_MSEC = 259200000;
    private final static String REFRESH_TOKEN = "refresh_token";

/*    @GetMapping("/refresh")
    public APIResponse refreshToken (HttpServletRequest request, HttpServletResponse response) {
        // access token 확인
        String accessToken = HeaderUtil.getAccessToken(request);
        AuthToken authToken = tokenProvider.convertAuthToken(accessToken);
        if (!authToken.validate()) {
            return APIResponse.invalidAccessToken();
        }

        // expired access token 인지 확인
        Claims claims = authToken.getExpiredTokenClaims();
        if (claims == null) {
            return APIResponse.notExpiredTokenYet();
        }

        String userId = claims.getSubject();
        RoleType roleType = RoleType.of(claims.get("role", String.class));

        // refresh token
        String refreshToken = CookieUtil.getCookie(request, REFRESH_TOKEN)
                .map(Cookie::getValue)
                .orElse((null));
        AuthToken authRefreshToken = tokenProvider.convertAuthToken(refreshToken);

        if (authRefreshToken.validate()) {
            System.out.println("만료된 토큰들어옴");
            return APIResponse.invalidRefreshToken();
        }

        // userId refresh token 으로 DB 확인
        CustomerRefreshToken userRefreshToken = customerRefreshTokenRepository.findByIdAndRefreshToken(userId, refreshToken);
        if (userRefreshToken == null) {
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
            userRefreshToken.setRefreshToken(authRefreshToken.getToken());

            int cookieMaxAge = (int) refreshTokenExpiry / 60;
            CookieUtil.deleteCookie(request, response, REFRESH_TOKEN);
            CookieUtil.addCookie(response, REFRESH_TOKEN, authRefreshToken.getToken(), cookieMaxAge);
        }

        return APIResponse.success("token", newAccessToken.getToken());
    }*/

    @PostMapping("/refresh")
    public APIResponse refreshToken (HttpServletRequest request) throws Exception {
        // access token 확인
        String accessToken = HeaderUtil.getAccessToken(request);
        AuthToken authToken = tokenProvider.convertAuthToken(accessToken);

        if (!authToken.validate()) {
            return APIResponse.invalidAccessToken();
        }

        // expired access token 인지 확인
        Claims claims = authToken.getExpiredTokenClaims();
        if (claims == null) {
            return APIResponse.notExpiredTokenYet();
        }

        String userId = claims.getSubject();
        RoleType roleType = RoleType.of(claims.get("role", String.class));

        // refresh token cookie에서 갖고옴
        String refreshToken = CookieUtil.getCookie(request, REFRESH_TOKEN).orElseThrow(() -> new Exception("cookie token invalidate")).getValue();

        AuthToken authRefreshToken = tokenProvider.convertAuthToken(refreshToken);

        if (authRefreshToken.validate()) {
            return APIResponse.invalidRefreshToken();
        }

        // userId refresh token으로 DB 확인
        CustomerRefreshToken userRefreshToken = customerRefreshTokenRepository.findByIdAndRefreshToken(userId, refreshToken);
        if (userRefreshToken == null) {
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
            userRefreshToken.setRefreshToken(authRefreshToken.getToken());
            customerRefreshTokenRepository.save(userRefreshToken);
        }

        return APIResponse.success("token", newAccessToken.getToken());
    }


//    @PostMapping("/signup")
//    public ResponseEntity<Customer> signup(@RequestBody Customer customer){
//        Customer createdCustomer = customerService.signup(customer);
//        return new ResponseEntity<>(createdCustomer, HttpStatus.CREATED);
//    }
//
//    @PostMapping("/local")
//    public ResponseEntity<String> login(@RequestBody Customer customer){
//        Customer savedCustomer = customerService.login(customer);
//        Date expireDate= new Date(new Date().getTime() + appProperties.getAuth().getTokenExpiry());
//        // subj role exp
//        AuthToken authToken = tokenProvider.createAuthToken(customer.getId(), RoleType.USER.getCode(), expireDate);
//        return ResponseEntity.ok(authToken.getToken());
//    }
//
//    @DeleteMapping("/quit")
//    public ResponseEntity<String> quit(HttpServletRequest request){
//        // Step 1 :: Header 에서 토큰 꺼내옴
//        String accessToken = HeaderUtil.getAccessToken(request);
//        // Step 2 :: 토큰 값으로 authToken 객체 변환
//        AuthToken authToken = tokenProvider.convertAuthToken(accessToken);
//        // expired access token 인지 확인
//
//        // Claims
//        Claims claims = authToken.getTokenClaims();
//        String userId = claims.getId();
//
//        String refreshToken = CookieUtil.getCookie(request, REFRESH_TOKEN)
//                .map(Cookie::getValue)
//                .orElse((null));
//        AuthToken authRefreshToken = tokenProvider.convertAuthToken(refreshToken);
//
//
//        return ResponseEntity.ok("success");
//    }


}