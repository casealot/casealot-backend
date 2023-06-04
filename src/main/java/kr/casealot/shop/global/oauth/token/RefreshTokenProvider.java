package kr.casealot.shop.global.oauth.token;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import kr.casealot.shop.global.oauth.entity.RoleType;
import kr.casealot.shop.global.oauth.exception.TokenValidFailedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.Key;
import java.sql.Ref;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;


@Slf4j
public class RefreshTokenProvider {

    private final Key key;
    private static final String AUTHORITIES_KEY = "role";

    public RefreshTokenProvider(String secret) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    public RefreshToken createAuthToken(String id, Date expiry) {
        return new RefreshToken(id, expiry, key);
    }

    public RefreshToken createAuthToken(String id, String role, Date expiry) {
        return new RefreshToken(id, role, expiry, key);
    }

    public RefreshToken convertAuthToken(String token) {
        return new RefreshToken(token, key);
    }

    public Authentication getAuthentication(RefreshToken refreshToken) {

        if (refreshToken.validate()) {

            Claims claims = refreshToken.getTokenClaims();
            Collection<? extends GrantedAuthority> authorities =
                    Arrays.stream(new String[]{claims.get(AUTHORITIES_KEY).toString()})
                            .map(SimpleGrantedAuthority::new)
                            .collect(Collectors.toList());

            log.debug("claims subject := [{}]", claims.getSubject());
            UserDetails principal = new User(claims.getSubject(), "", authorities);

            return new UsernamePasswordAuthenticationToken(principal, refreshToken, authorities);
        } else {
            throw new TokenValidFailedException();
        }
    }
}
