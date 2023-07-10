package kr.casealot.shop.global.oauth.service;

import kr.casealot.shop.domain.customer.entity.Customer;
import kr.casealot.shop.domain.customer.repository.CustomerRepository;
import kr.casealot.shop.global.oauth.entity.ProviderType;
import kr.casealot.shop.global.oauth.entity.RoleType;
import kr.casealot.shop.global.oauth.entity.UserPrincipal;
import kr.casealot.shop.global.oauth.exception.OAuthProviderMissMatchException;
import kr.casealot.shop.global.oauth.info.OAuth2UserInfo;
import kr.casealot.shop.global.oauth.info.OAuth2UserInfoFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final CustomerRepository customerRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User user = super.loadUser(userRequest);
        return this.process(userRequest, user);
    }

    private OAuth2User process(OAuth2UserRequest userRequest, OAuth2User user) {
        ProviderType providerType = ProviderType.valueOf(userRequest.getClientRegistration().getRegistrationId().toUpperCase());

        OAuth2UserInfo userInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(providerType, user.getAttributes());
        // providerType과 userId를 혼합하여 ID로 지정한다.
        Customer savedCustomer = customerRepository.findById(providerType.getValue() + userInfo.getId());

        if (savedCustomer != null) {
            if (providerType != savedCustomer.getProviderType()) {
                throw new OAuthProviderMissMatchException(
                        "Looks like you're signed up with " + providerType +
                        " account. Please use your " + savedCustomer.getProviderType() + " account to login."
                );
            }
            updateUser(savedCustomer, userInfo);
        } else {
            savedCustomer = createCustomer(userInfo, providerType);
        }

        return UserPrincipal.create(savedCustomer, user.getAttributes());
    }

    private Customer createCustomer(OAuth2UserInfo userInfo, ProviderType providerType) {
        LocalDateTime now = LocalDateTime.now();
        // providerType과 userId를 혼합하여 ID로 지정한다.
        Customer user = new Customer(
                providerType.getValue() + userInfo.getId(),
                userInfo.getName(),
                userInfo.getEmail(),
                "Y",
                userInfo.getImageUrl(),
                userInfo.getMobile(),
                providerType,
                RoleType.USER
        );

        return customerRepository.saveAndFlush(user);
    }

    private Customer updateUser(Customer customer, OAuth2UserInfo userInfo) {
        // 이름이 바뀔경우
        if (userInfo.getName() != null && !customer.getName().equals(userInfo.getName())) {
            customer.setName(userInfo.getName());
        }

        // 이미지가 바뀔경우
        if (userInfo.getImageUrl() != null && !customer.getProfileImageUrl().equals(userInfo.getImageUrl())) {
            customer.setProfileImageUrl(userInfo.getImageUrl());
        }

        //
        if (userInfo.getMobile() != null && !customer.getPhoneNumber().equals(userInfo.getMobile())) {
            customer.setPhoneNumber(userInfo.getMobile());
        }

        return customer;
    }
}
