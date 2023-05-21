package kr.casealot.shop.global.oauth.info;

import kr.casealot.shop.global.oauth.entity.ProviderType;
import kr.casealot.shop.global.oauth.info.impl.KakaoOAuth2UserInfo;
import kr.casealot.shop.global.oauth.info.impl.NaverOAuth2UserInfo;

import java.util.Map;

public class OAuth2UserInfoFactory {
    public static OAuth2UserInfo getOAuth2UserInfo(ProviderType providerType, Map<String, Object> attributes) {
        // provider Type 별로 해당하는 User정보를 갖고온다.
        switch (providerType) {
            case NAVER: return new NaverOAuth2UserInfo(attributes);
            case KAKAO: return new KakaoOAuth2UserInfo(attributes);
            default: throw new IllegalArgumentException("Invalid Provider Type.");
        }
    }
}