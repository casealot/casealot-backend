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
import kr.casealot.shop.global.common.APIResponseHeader;
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

    public Long join(CustomerDto customerDto) {
        String encodedPassword = passwordEncoder.encode(customerDto.getPassword());

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
        return savedCustomer.getSeq();
    }

    public CustomerTokenDto login(CustomerLoginDto customerLoginDto) {
        Customer customer = customerRepository.findById(customerLoginDto.getId());

        if (customer == null) {
            throw new IllegalArgumentException("ID" + customerLoginDto.getId() + "인 사용자를 찾을 수 없습니다.");
        }

        // 비밀번호 일치 확인
        if (!passwordEncoder.matches(customerLoginDto.getPassword(), customer.getPassword())) {
            throw new IllegalArgumentException("잘못된 비밀번호입니다.");
        }

        // 토큰 유효 기간 설정 (1시간 후) (테스트용 24시간으로 늘림)
        Date jwtExpiry = new Date((System.currentTimeMillis() + 3600000) * 24);

        // refreshToken 기간 2주 설정
        Date refreshExpiry = new Date((System.currentTimeMillis() + 3600000) * 24 * 14);

        // 토큰 생성 (jwt)
        AuthToken authToken = authTokenProvider.createAuthToken(customer.getId(), RoleType.USER.getCode(), jwtExpiry);
        AuthToken refToken = authTokenProvider.createAuthToken(customer.getId(), RoleType.USER.getCode(), refreshExpiry);

        String accessToken = authToken.getToken();
        String refreshToken = refToken.getToken();

        // 토큰 생성 (refresh)
        CustomerRefreshToken customerRefreshToken = new CustomerRefreshToken(customer.getId(), refreshToken);
        customerRefreshTokenRepository.save(customerRefreshToken);

        return new CustomerTokenDto(customer.getId(), accessToken, refreshToken, customer.getRoleType());
    }

    //로그아웃
    @Transactional
    public void logout(HttpServletRequest request) {
        String token = HeaderUtil.getAccessToken(request);

        AuthToken authToken = authTokenProvider.convertAuthToken(token);

        Claims claims = authToken.getTokenClaims();
        String userId = claims.getSubject();

        System.out.println("ID: " + userId);

        CustomerRefreshToken customerRefreshToken = customerRefreshTokenRepository.findById(userId);
        if (customerRefreshToken != null) {
            customerRefreshTokenRepository.deleteById(customerRefreshToken.getRefreshTokenSeq());
        }
    }

    @Transactional
    public long deleteCustomer(HttpServletRequest request) {
        String token = HeaderUtil.getAccessToken(request);

        AuthToken authToken = authTokenProvider.convertAuthToken(token);

        Claims claims = authToken.getTokenClaims();
        String userId = claims.getSubject();

        System.out.println("ID: " + userId);

        return customerRepository.deleteById(userId);
    }
}