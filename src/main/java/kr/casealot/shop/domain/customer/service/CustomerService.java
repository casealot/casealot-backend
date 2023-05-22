package kr.casealot.shop.domain.customer.service;

import kr.casealot.shop.domain.customer.dto.CustomerDto;
import kr.casealot.shop.domain.customer.entity.Customer;
import kr.casealot.shop.domain.customer.repository.CustomerRepository;
import kr.casealot.shop.global.oauth.entity.ProviderType;
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
                .build();

        Customer savedCustomer = customerRepository.save(customer);
        return savedCustomer.getSeq();
    }

    public AuthToken login(CustomerDto customerDto) {
        Customer customer = customerRepository.findById(customerDto.getId());

        if (!passwordEncoder.matches(customerDto.getPassword(), customer.getPassword())) {
            throw new IllegalArgumentException("잘못된 비밀번호입니다.");
        }
        //토큰 유효기간
        Date expiry = new Date(System.currentTimeMillis() + 3600000);

        // 토큰 생성
        AuthToken token = authTokenProvider.createAuthToken(customer.getId(), expiry);

        return token;
    }


}
