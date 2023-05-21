package kr.casealot.shop.oauth;

import kr.casealot.shop.global.oauth.token.AuthToken;
import kr.casealot.shop.global.oauth.token.AuthTokenProvider;
import org.junit.jupiter.api.Test;

import java.util.Date;

public class OAuthTest {
    final String key = "casealotcasealotcasealotcasealotcasealotcasealotcasealotcasealotcasealotcasealot";
    @Test
    void JWT_토큰발급(){
        AuthTokenProvider provider = new AuthTokenProvider(key);
        AuthToken authToken = provider.createAuthToken("test", new Date());
        System.out.println(authToken.toString());
    }
}
