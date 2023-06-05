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
import kr.casealot.shop.global.oauth.entity.RoleType;
import kr.casealot.shop.global.oauth.token.AuthToken;
import kr.casealot.shop.global.oauth.token.AuthTokenProvider;
import kr.casealot.shop.global.util.HeaderUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final CustomerTokenRepository customerTokenRepository;
    private final CustomerRefreshTokenRepository customerRefreshTokenRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final AuthTokenProvider authTokenProvider;

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
                .roleType(RoleType.ADMIN)
                .build();

        Customer savedCustomer = customerRepository.save(customer);

        return APIResponse.success("customer", customer);
    }

    public APIResponse login(CustomerLoginDto customerLoginDto) {
        Customer customer = customerRepository.findById(customerLoginDto.getId());

        if (customer == null) {
            return APIResponse.incorrectID();
        }

        // 비밀번호 일치 확인
        if (!passwordEncoder.matches(customerLoginDto.getPassword(), customer.getPassword())) {
            return APIResponse.incorrectPassword();
        }

        // 토큰 유효 기간 설정 (1시간 후) (테스트용 24시간으로 늘림)
        Date jwtExpiry = new Date((System.currentTimeMillis() + 3600000));

        // refreshToken 기간 2주 설정
        Date refreshExpiry = new Date((System.currentTimeMillis() + 3600000) * 24 * 14);

        // 토큰 생성 (jwt)
        AuthToken authToken = authTokenProvider.createAuthToken(customer.getId(), RoleType.ADMIN.getCode(), jwtExpiry);

        String accessToken = authToken.getToken();

        AuthToken refreshToken = authTokenProvider.createAuthToken(
                RoleType.USER.getCode(),
                refreshExpiry
        );

        // userId refresh token 으로 DB 확인
        CustomerRefreshToken userRefreshToken = customerRefreshTokenRepository.findById(customer.getId());
        if (userRefreshToken == null) {
            // 없는 경우 새로 등록
            userRefreshToken = new CustomerRefreshToken(customer.getId(), refreshToken.getToken());
            customerRefreshTokenRepository.saveAndFlush(userRefreshToken);
        } else {
            // DB에 refresh 토큰 업데이트
            userRefreshToken.setRefreshToken(refreshToken.getToken());
        }

//        // 토큰 생성 (refresh)
//        CustomerRefreshToken customerRefreshToken = new CustomerRefreshToken(customer.getId(), refreshToken);
//        customerRefreshTokenRepository.save(customerRefreshToken);

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