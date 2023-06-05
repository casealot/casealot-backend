package kr.casealot.shop.domain.customer.service;

import io.jsonwebtoken.Claims;
import kr.casealot.shop.domain.auth.entity.CustomerRefreshToken;
import kr.casealot.shop.domain.auth.repository.CustomerRefreshTokenRepository;
import kr.casealot.shop.domain.auth.repository.CustomerTokenRepository;
import kr.casealot.shop.domain.customer.dto.CustomerDto;
import kr.casealot.shop.domain.customer.dto.CustomerLoginDto;
import kr.casealot.shop.domain.customer.dto.CustomerTokenDto;
import kr.casealot.shop.domain.customer.entity.Customer;
import kr.casealot.shop.domain.customer.repository.CustomerRepository;
import kr.casealot.shop.global.common.APIResponse;
import kr.casealot.shop.global.config.properties.AppProperties;
import kr.casealot.shop.global.oauth.entity.RoleType;
import kr.casealot.shop.global.oauth.token.AuthToken;
import kr.casealot.shop.global.oauth.token.AuthTokenProvider;
import kr.casealot.shop.global.util.CookieUtil;
import kr.casealot.shop.global.util.HeaderUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

import static org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames.REFRESH_TOKEN;

@Service
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;
    //private final CustomerTokenRepository customerTokenRepository;

    private final AppProperties appProperties;
    private final CustomerRefreshTokenRepository customerRefreshTokenRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final AuthTokenProvider authTokenProvider;

    private final static String REFRESH_TOKEN = "refresh_token";


    public APIResponse join(CustomerDto customerDto) {
        String encodedPassword = passwordEncoder.encode(customerDto.getPassword());

        // 아이디 중복 확인
        if (customerRepository.existsCustomerById(customerDto.getId())) {
            return APIResponse.incorrectID();
        }

        // 이메일 중복 확인
        if (customerRepository.existsByEmail(customerDto.getEmail())) {
            return APIResponse.duplicatedEmail();
        }

        Customer customer = Customer.builder()
                .id(customerDto.getId())
                .name(customerDto.getName())
                .password(encodedPassword)
                .phoneNumber(customerDto.getPhoneNumber())
                .email(customerDto.getEmail())
                .profileImageUrl(customerDto.getProfileImageUrl())
                .postNo(customerDto.getPostNo())
                .address(customerDto.getAddress())
                .addressDetail(customerDto.getAddressDetail())
                .roleType(RoleType.USER)
                .build();

        Customer savedCustomer = customerRepository.save(customer);

        return APIResponse.success("customer", customer);
    }

    public APIResponse login(CustomerLoginDto customerLoginDto
            , HttpServletRequest request
            , HttpServletResponse response) {
        Customer customer = customerRepository.findById(customerLoginDto.getId());

        if (customer == null) {
            return APIResponse.incorrectID();
        }

        // 비밀번호 일치 확인
        if (!passwordEncoder.matches(customerLoginDto.getPassword(), customer.getPassword())) {
            return APIResponse.incorrectPassword();
        }

        Date now = new Date();

        // 토큰 생성 (jwt)
        // 토큰 유효 기간 설정 (30분 후)
        long jwtExpiry = now.getTime() + appProperties.getAuth().getTokenExpiry();
        AuthToken authToken = authTokenProvider.createAuthToken(customer.getId(), RoleType.USER.getCode(), new Date(jwtExpiry));

        String accessToken = authToken.getToken();

        // refreshToken 기간 7일
        long refreshExpiry = appProperties.getAuth().getRefreshTokenExpiry();
        AuthToken refreshToken = authTokenProvider.createAuthToken(
                RoleType.USER.getCode(),
                new Date(refreshExpiry)
        );

        // userId refresh token 으로 DB 확인
        CustomerRefreshToken userRefreshToken = customerRefreshTokenRepository.findById(customer.getId());
        if (userRefreshToken == null) {
            // 없는 경우 새로 등록
            userRefreshToken = new CustomerRefreshToken(customer.getId(), refreshToken.getToken());
        } else {
            // TODO DB에 refresh 토큰 업데이트
            userRefreshToken.setRefreshToken(refreshToken.getToken());
        }

        customerRefreshTokenRepository.saveAndFlush(userRefreshToken);

        int cookieMaxAge = (int) refreshExpiry;
        CookieUtil.deleteCookie(request, response, REFRESH_TOKEN);
        CookieUtil.addCookie(response, REFRESH_TOKEN, userRefreshToken.getRefreshToken(), cookieMaxAge);

        return APIResponse.success("customerToken", new CustomerTokenDto(customer.getId(), accessToken, userRefreshToken.getRefreshToken(), customer.getRoleType()));
    }

    //로그아웃
    @Transactional
    public APIResponse logout(HttpServletRequest request) {
        String token = HeaderUtil.getAccessToken(request);

        AuthToken authToken = authTokenProvider.convertAuthToken(token);

        Claims claims = authToken.getTokenClaims();
        String userId = claims.getSubject();

        System.out.println("ID: " + userId);

        //회원탈퇴시에 refreshToken 삭제
        //TODO: accessToken 사용불가하게 만들어야함.
        CustomerRefreshToken customerRefreshToken = customerRefreshTokenRepository.findById(userId);
        if (customerRefreshToken != null) {
            customerRefreshTokenRepository.deleteById(customerRefreshToken.getRefreshTokenSeq());
        }

        return APIResponse.success("customerId", userId);
    }


    @Transactional
    public APIResponse deleteCustomer(HttpServletRequest request) {
        String token = HeaderUtil.getAccessToken(request);

        AuthToken authToken = authTokenProvider.convertAuthToken(token);

        Claims claims = authToken.getTokenClaims();
        if (claims == null) {
            return APIResponse.invalidAccessToken();
        }
        String userId = claims.getSubject();

        //존재하는 회원인지 확인
        if (customerRepository.existsCustomerById(userId)) {
            return APIResponse.invalidAccessToken();
        }

        System.out.println("ID: " + userId);

        return APIResponse.success("customerId", userId);
    }
}