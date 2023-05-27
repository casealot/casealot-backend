package kr.casealot.shop.domain.customer.service;

import kr.casealot.shop.domain.customer.dto.CustomerDto;
import kr.casealot.shop.domain.customer.entity.Customer;
import kr.casealot.shop.domain.customer.repository.CustomerRepository;
import kr.casealot.shop.global.oauth.entity.RoleType;
import kr.casealot.shop.global.oauth.token.AuthToken;
import kr.casealot.shop.global.oauth.token.AuthTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final AuthTokenProvider authTokenProvider;

    public Long join(CustomerDto customerDto) {
        String encodedPassword = passwordEncoder.encode(customerDto.getPassword());

        Customer customer = Customer.builder()
                .id(customerDto.getId())
                .name(customerDto.getName())
                .password(encodedPassword)
                .email(customerDto.getEmail())
                .profileImageUrl(customerDto.getProfileImageUrl())
                .address(customerDto.getAddress())
                .addressDetail(customerDto.getAddressDetail())
                .roleType(RoleType.USER)
                .build();

        Customer savedCustomer = customerRepository.save(customer);
        return savedCustomer.getSeq();
    }

    public AuthToken login(CustomerDto customerDto) {
        Customer customer = customerRepository.findById(customerDto.getId());

        if (customer == null) {
            throw new IllegalArgumentException("사용자를 찾을 수 없습니다.");
        }

        // 비밀번호 일치 확인
        if (!passwordEncoder.matches(customerDto.getPassword(), customer.getPassword())) {
            throw new IllegalArgumentException("잘못된 비밀번호입니다.");
        }

        // 토큰 유효 기간 설정 (1시간 후)
        Date expiry = new Date(System.currentTimeMillis() + 3600000);

        // 토큰 생성
        AuthToken token = authTokenProvider.createAuthToken(customer.getId(), expiry);

        return token;
    }

    public boolean deleteCustomer(String token) {
        //TODO: 토큰으로 user의 Id를 알아내어 Customer 정보를 삭제해야 한다
        AuthToken authToken = authTokenProvider.convertAuthToken(token);
        // 토큰에서 사용자 아이디 추출
        String userId = authToken.getTokenClaims().getSubject();

        // 아이디로 사용자 삭제
        boolean deleted = customerRepository.deleteById(userId);

        return deleted;
    }


}
