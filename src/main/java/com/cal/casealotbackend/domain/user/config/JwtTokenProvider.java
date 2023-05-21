package com.cal.casealotbackend.domain.user.config;

import com.cal.casealotbackend.domain.user.domain.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
@Component
public class JwtTokenProvider {

    //TODO: yml 파일에 키 값 지정하고 가져와햠
    @Value("{spring.jwt.secret}")
    private String secretKey;

    public static final String TOKEN_HEADER = "Authorization"; //토큰헤더
    public static final String TOKEN_PREFIX = "Bearer "; //jwt토큰 사용하는 경우 Bearer + 한칸띄고 붙임

    // 결론적으로 Authorization | Bearer +{___}

    // 토큰 유효시간 1시간
    private long tokenValidTime = 1000 * 60 * 60; //1시간(1000 * 60 * 60)

    private final UserDetailsService userDetailsService;

    // 객체 초기화, secretKey를 Base64로 인코딩한다.
    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    // JWT 토큰 생성
    public String createToken(String userPk, List<String> roles) {
        Claims claims = Jwts.claims().setSubject(userPk); // JWT payload 에 저장되는 정보단위
        claims.put("roles", roles); // 정보는 key / value 쌍으로 저장된다.
        Date now = new Date();
        return Jwts.builder()
                .setClaims(claims) // 정보 저장
                .setIssuedAt(now) // 토큰 발행 시간 정보
                .setExpiration(new Date(now.getTime() + tokenValidTime)) // set Expire Time
                .signWith(SignatureAlgorithm.HS512, secretKey)  // 사용할 암호화 알고리즘과
                // signature 에 들어갈 secret값 세팅
                .compact();
    }

    // JWT 토큰에서 인증 정보 조회
    public Authentication getAuthentication(String token) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(this.getUserPk(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    // 토큰에서 회원 정보 추출
    public String getUserPk(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }

    // Request의 Header에서 token 값을 가져옵니다. "Authorization" : "Bearer +TOKEN값'
    public String resolveToken(HttpServletRequest request) {
        String token = request.getHeader((TOKEN_HEADER));

        if (!ObjectUtils.isEmpty(token) && token.startsWith(TOKEN_PREFIX)) { //토큰형태 포함
            return token.substring(TOKEN_PREFIX.length()); //실제토큰 부위
        }

        return null;
    }

    // 토큰의 유효성 + 만료일자 확인
    public boolean validateToken(String jwtToken) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwtToken);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    //권한이 ADMIN인지 확인 하는 기능
    public boolean userHasAdminRole(String token) {
        String userPk = getUserPk(token);
        UserDetails userDetails = userDetailsService.loadUserByUsername(userPk);
        // 사용자의 권한 중에 "ADMIN"이 있는지 확인합니다.
        return userDetails.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ADMIN"));
    }

    //권한이 USER인지 확인 하는 기능
    public boolean userHasUserRole(String token) {
        String userPk = getUserPk(token);
        UserDetails userDetails = userDetailsService.loadUserByUsername(userPk);
        // 사용자의 권한 중에 "USER"이 있는지 확인합니다.
        return userDetails.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("USER"));
    }

    // 토큰에서 userName 정보 추출
    public String getUserPhoneNum(String token) {
        String userPk = getUserPk(token); // 토큰에서 userPk 추출
        // userPk를 사용하여 userName 찾기
        UserDetails userDetails = userDetailsService.loadUserByUsername(userPk);
        if (userDetails instanceof User) {
            User user = (User) userDetails;
            return user.getUserName(); // userName 반환
        }
        return null;
    }
}