package kr.casealot.shop.config;

import kr.casealot.shop.oauth.token.AuthTokenProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author kimchanghee
 */
@Configuration
public class JwtConfig {
    @Value("${jwt.secret-key")
    private String secretKey;

    @Bean
    public AuthTokenProvider jwtProvider() {
        return new AuthTokenProvider(secretKey);
    }

}
