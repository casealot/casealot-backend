package kr.casealot.shop.global.oauth.filter;

import kr.casealot.shop.domain.auth.entity.BlacklistToken;
import kr.casealot.shop.domain.auth.repository.BlacklistTokenRepository;
import kr.casealot.shop.global.oauth.service.CustomUserDetailsService;
import kr.casealot.shop.global.oauth.token.AuthToken;
import kr.casealot.shop.global.oauth.token.AuthTokenProvider;
import kr.casealot.shop.global.util.HeaderUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class TokenAuthenticationFilter extends OncePerRequestFilter {
    private final AuthTokenProvider tokenProvider;
    private final CustomUserDetailsService customUserDetailsService;
    private final BlacklistTokenRepository blacklistTokenRepository;
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)  throws ServletException, IOException {

        String tokenStr = HeaderUtil.getAccessToken(request);
        AuthToken token = tokenProvider.convertAuthToken(tokenStr);

        if (token != null && token.validate() && !isTokenBlacklisted(tokenStr)) {
            Authentication authentication = tokenProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }

    private boolean isTokenBlacklisted(String token) {
        BlacklistToken blacklistToken = blacklistTokenRepository.findByBlacklistToken(token);
        return blacklistToken != null;
    }
}
