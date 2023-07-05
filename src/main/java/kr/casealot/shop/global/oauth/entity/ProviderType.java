package kr.casealot.shop.global.oauth.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author kimchanghee
 */
@Getter
@AllArgsConstructor
public enum ProviderType {
    LOCAL("LOCAL"),
    NAVER("NAVER"),
    KAKAO("KAKAO");

    private final String value;

}
