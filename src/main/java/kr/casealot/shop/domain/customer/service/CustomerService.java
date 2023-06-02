package kr.casealot.shop.domain.customer.service;

import io.jsonwebtoken.Claims;
import kr.casealot.shop.domain.auth.entity.CustomerToken;
import kr.casealot.shop.domain.auth.repository.CustomerTokenRepository;
import kr.casealot.shop.domain.customer.dto.CustomerDto;
import kr.casealot.shop.domain.customer.dto.CustomerLoginDto;
import kr.casealot.shop.domain.customer.entity.Customer;
import kr.casealot.shop.domain.customer.repository.CustomerRepository;
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
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final CustomerTokenRepository customerTokenRepository;
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
                .address(customerDto.getAddress())
                .addressDetail(customerDto.getAddressDetail())
                .roleType(customerDto.getRoleType())
                .build();

        Customer savedCustomer = customerRepository.save(customer);
        return savedCustomer.getSeq();
    }

    public CustomerToken login(CustomerLoginDto customerLoginDto) {
        Customer customer = customerRepository.findById(customerLoginDto.getId());

        if (customer == null) {
            throw new IllegalArgumentException("ID가 " + customerLoginDto.getId() + "인 사용자를 찾을 수 없습니다.");
        }

        Long customerSeq = customer.getSeq();

        // 비밀번호 일치 확인
        if (!passwordEncoder.matches(customerLoginDto.getPassword(), customer.getPassword())) {
            throw new IllegalArgumentException("잘못된 비밀번호입니다.");
        }

        // 토큰 유효 기간 설정 (1시간 후)
        Date expiry = new Date(System.currentTimeMillis() + 3600000);

        // 토큰 생성 (jwt)
        AuthToken authToken = authTokenProvider.createAuthToken(customer.getId(), RoleType.USER.getCode(), expiry);
        String accessToken = authToken.getToken();

        // 토큰 생성 (refresh)
        String refreshToken = String.valueOf(UUID.randomUUID());

        CustomerToken customerToken = new CustomerToken(customerSeq, accessToken, refreshToken);

        customerTokenRepository.save(customerToken);

        return customerToken;
    }

    @Transactional
    public void logout(HttpServletRequest request) {
        String token = HeaderUtil.getAccessToken(request);
        AuthToken authToken = authTokenProvider.convertAuthToken(token);

        Claims claims = authToken.getTokenClaims();
        String userId = claims.getSubject();

        Customer customer = customerRepository.findCustomerById(userId);
        Long customerSeq = customer.getSeq();

        customerTokenRepository.deleteByCustomerSeq(customerSeq);
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