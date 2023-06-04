package kr.casealot.shop.global.oauth.token;

import io.jsonwebtoken.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.security.Key;
import java.util.Date;

/**
 * @Author IamAnjaehyun
 */
@Slf4j
@RequiredArgsConstructor
@ToString
public class RefreshToken {

    @Getter
    private final String token;
    private final Key key;

    private static final String AUTHORITIES_KEY = "role";

    RefreshToken(String id, Date expiry, Key key) {
        this.key = key;
        this.token = createRefreshToken(id, expiry);
    }

    RefreshToken(String id, String role, Date expiry, Key key) {
        this.key = key;
        this.token = createRefreshToken(id, role, expiry);
    }

    private String createRefreshToken(String id, Date expiry) {
        return Jwts.builder()
                .setSubject(id)
                .signWith(key, SignatureAlgorithm.HS256)
                .setExpiration(expiry)
                .compact();
    }


    private String createRefreshToken(String id, String role, Date expiry) {
        return Jwts.builder()
                .setSubject(id)
                .claim(AUTHORITIES_KEY, role)
                .signWith(key, SignatureAlgorithm.HS256)
                .setExpiration(expiry)
                .compact();
    }

    public boolean validate() {
        return this.getTokenClaims() != null;
    }

    public Claims getTokenClaims() {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (SecurityException e) {
            log.info("Invalid RefreshToken signature.");
        } catch (MalformedJwtException e) {
            log.info("Invalid RefreshToken.");
        } catch (ExpiredJwtException e) {
            log.info("Expired RefreshToken.");
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported RefreshToken.");
        } catch (IllegalArgumentException e) {
            log.info("RefreshToken compact of handler are invalid.");
        }
        return null;
    }

    public Claims getExpiredTokenClaims() {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            log.info("Expired RefreshToken.");
            return e.getClaims();
        }
        return null;
    }
}
