package kr.casealot.shop.domain.customer.service;

import io.jsonwebtoken.Claims;
import kr.casealot.shop.domain.auth.entity.CustomerRefreshToken;
import kr.casealot.shop.domain.auth.repository.CustomerRefreshTokenRepository;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Principal;
import java.util.Date;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerService {
    private final String API_NAME = "customer";

    private final CustomerRepository customerRepository;

    private final AppProperties appProperties;
    private final CustomerRefreshTokenRepository customerRefreshTokenRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final AuthTokenProvider authTokenProvider;
    private final AuthTokenProvider tokenProvider;
    private final static String REFRESH_TOKEN = "refreshToken";

    public APIResponse<String> join(CustomerDto customerDto) {
        String encodedPassword = passwordEncoder.encode(customerDto.getPassword());

        // 아이디 중복 확인
        if (customerRepository.existsCustomerById(customerDto.getId())) {
            return APIResponse.duplicatedID();
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

        customerRepository.save(customer);

        return APIResponse.success(API_NAME, customer.getId() + " join success!");
    }

    public APIResponse<CustomerTokenDto> login(CustomerLoginDto customerLoginDto
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

        RoleType roleType = customer.getRoleType();

        Date now = new Date();
        // 토큰 생성 (jwt)
        // 토큰 유효 기간 설정 (30분 후)
        //long jwtExpiry = now.getTime() + appProperties.getAuth().getTokenExpiry() + (60 * 60 * 24 * 30); //1달로 설정
        long jwtExpiry = now.getTime() + 1000 * 60 * 2; // 테스트 2분

        AuthToken authToken = authTokenProvider.createAuthToken(
                customer.getId(),
                roleType.getCode(),
                new Date(jwtExpiry));

        String accessToken = authToken.getToken();

        // userId refresh token 으로 DB 확인
        CustomerRefreshToken customerRefreshToken = customerRefreshTokenRepository.findById(customer.getId());

        long refreshTokenExpiry = appProperties.getAuth().getRefreshTokenExpiry();

        AuthToken refreshToken = null;
        if (customerRefreshToken != null) {
            refreshToken = authTokenProvider.convertAuthToken(customerRefreshToken.getRefreshToken());
            // refresh 토큰이 유효 됐을 경우 다시 재발급 한다.
            if(refreshToken.validate()){
                refreshToken = tokenProvider.createAuthToken(
                        appProperties.getAuth().getTokenSecret(),
                        new Date(now.getTime() + refreshTokenExpiry)
                );
                customerRefreshToken.setRefreshToken(refreshToken.getToken());
                customerRefreshTokenRepository.saveAndFlush(customerRefreshToken);
            }
        } else {
            refreshToken = tokenProvider.createAuthToken(
                    appProperties.getAuth().getTokenSecret(),
                    new Date(now.getTime() + refreshTokenExpiry)
            );
            customerRefreshToken.setRefreshToken(refreshToken.getToken());
            customerRefreshToken = new CustomerRefreshToken(customer.getId(), refreshToken.getToken());
            customerRefreshTokenRepository.saveAndFlush(customerRefreshToken);
        }

        int cookieMaxAge = (int) refreshTokenExpiry / 60;
        CookieUtil.deleteCookie(request, response, REFRESH_TOKEN);
        CookieUtil.addCookie(response, REFRESH_TOKEN, refreshToken.getToken(), cookieMaxAge);

        return APIResponse.success("customerToken", new CustomerTokenDto(customer.getId(), accessToken, customerRefreshToken.getRefreshToken(), customer.getRoleType()));
    }

    //로그아웃
    @Transactional
    public APIResponse<String> logout(HttpServletRequest request) {
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

        return APIResponse.success("customerId", userId + " user logout!");
    }


    @Transactional
    public APIResponse<String> deleteCustomer(HttpServletRequest request, Principal principal) {
        customerRepository.deleteById(principal.getName());

        log.info("ID: " + principal.getName() + "quit!");

        return APIResponse.success("customerId", principal.getName() + " user deleted !");
    }
}