package kr.casealot.shop.global.config.security;

import kr.casealot.shop.domain.auth.repository.BlacklistTokenRepository;
import kr.casealot.shop.domain.auth.repository.CustomerRefreshTokenRepository;
import kr.casealot.shop.domain.customer.repository.CustomerRepository;
import kr.casealot.shop.global.config.properties.AppProperties;
import kr.casealot.shop.global.oauth.exception.RestAuthenticationEntryPoint;
import kr.casealot.shop.global.oauth.filter.TokenAuthenticationFilter;
import kr.casealot.shop.global.oauth.handler.OAuth2AuthenticationFailureHandler;
import kr.casealot.shop.global.oauth.handler.OAuth2AuthenticationSuccessHandler;
import kr.casealot.shop.global.oauth.handler.TokenAccessDeniedHandler;
import kr.casealot.shop.global.oauth.repository.OAuth2AuthorizationRequestBasedOnCookieRepository;
import kr.casealot.shop.global.oauth.service.CustomOAuth2UserService;
import kr.casealot.shop.global.oauth.service.CustomUserDetailsService;
import kr.casealot.shop.global.oauth.token.AuthTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.SecurityExpressionHandler;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author KimChanghee
 */
@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  //    private final CorsProperties corsProperties;
  private final AppProperties appProperties;
  private final AuthTokenProvider tokenProvider;
  private final CustomUserDetailsService customerDetailsService;
  private final CustomOAuth2UserService oAuth2UserService;
  private final TokenAccessDeniedHandler tokenAccessDeniedHandler;
  private final CustomerRefreshTokenRepository customerRefreshTokenRepository;
  private final CustomerRepository customerRepository;
  private final CustomUserDetailsService customUserDetailsService;
  private final BlacklistTokenRepository blacklistTokenRepository;


  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(customerDetailsService)
        .passwordEncoder(passwordEncoder());
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.authorizeRequests()
        .antMatchers("/actuator/**").permitAll()
        .antMatchers("/explorer/**").permitAll()
        .antMatchers("/swagger-ui/**").permitAll()
        .antMatchers("/swagger-resources/**").permitAll()
        .antMatchers("/v2/api-docs/**").permitAll()
        .antMatchers("/h2-console/**").permitAll();

    http.cors().configurationSource(corsConfigurationSource())
        .and()
        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // SpringSecurity에서 세션 관리 X
        .and()
        .csrf().disable()
        .formLogin().disable()
        .httpBasic().disable()
        .exceptionHandling()
        .authenticationEntryPoint(new RestAuthenticationEntryPoint())
        .accessDeniedHandler(tokenAccessDeniedHandler)
        .and()
        .authorizeRequests().expressionHandler(expressionHandler())
        .antMatchers(
            "/health",
            "/cal/v1/auth/refresh",
            "/cal/v1/product/**",
            "/cal/v1/qna/list/**",
            "/cal/v1/notice/list/**",
            "/cal/v1/customer/join",
            "/cal/v1/customer/login",
                "/cal/v1/autocomplete"
        ).permitAll()
        .antMatchers(
            "/cal/v1/review/**",
            "/cal/v1/wishlist/**",
            "/cal/v1/notice/**",
            "/cal/v1/qna/**",
            "/cal/v1/cart/**",
                "/cal/v1/order/**",
                "/cal/v1/verifyIamport/**"
        ).hasRole("USER")
        .antMatchers(
            "/cal/v1/admin/**",
            "/cal/v1/admin/notice/**",
            "/cal/v1/admin/qna/**",
            "/cal/v1/file/**",
            "/cal/v1/function/**",
                "cal/v1/delivery/**"
        ).hasRole("ADMIN")
        .antMatchers("/",
            "/error",
            "/favicon.ico",
            "/**/*.png",
            "/**/*.gif",
            "/**/*.svg",
            "/**/*.jpg",
            "/**/*.html",
            "/**/*.css",
            "/**/*.js")
        .permitAll()
        .antMatchers("/oauth2/**")
        .permitAll()
        .anyRequest().authenticated()
        .and()
        .logout()
        .logoutSuccessUrl("/")
        .and()
        .oauth2Login()
        .authorizationEndpoint()
        .baseUri("/oauth2/authorization")
        .authorizationRequestRepository(oAuth2AuthorizationRequestBasedOnCookieRepository())
        .and()
        .redirectionEndpoint()
        .baseUri("/*/oauth2/code/*")
        .and()
        .userInfoEndpoint()
        .userService(oAuth2UserService)
        .and()
        .successHandler(oAuth2AuthenticationSuccessHandler())
        .failureHandler(oAuth2AuthenticationFailureHandler());
    http.addFilterBefore(tokenAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
  }

  /*
   * auth 매니저 설정
   * */
  @Override
  @Bean(BeanIds.AUTHENTICATION_MANAGER)
  protected AuthenticationManager authenticationManager() throws Exception {
    return super.authenticationManager();
  }

  /*
   * security 설정 시, 사용할 인코더 설정
   * */
  @Bean
  public BCryptPasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  /*
   * 토큰 필터 설정
   * */
  @Bean
  public TokenAuthenticationFilter tokenAuthenticationFilter() {
    return new TokenAuthenticationFilter(tokenProvider, customUserDetailsService,
        blacklistTokenRepository);
  }

  /*
   * 쿠키 기반 인가 Repository
   * 인가 응답을 연계 하고 검증할 때 사용.
   * */
  @Bean
  public OAuth2AuthorizationRequestBasedOnCookieRepository oAuth2AuthorizationRequestBasedOnCookieRepository() {
    return new OAuth2AuthorizationRequestBasedOnCookieRepository();
  }

  /*
   * Oauth 인증 성공 핸들러
   * */
  @Bean
  public OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler() {
    return new OAuth2AuthenticationSuccessHandler(
        tokenProvider,
        appProperties,
        customerRefreshTokenRepository,
        oAuth2AuthorizationRequestBasedOnCookieRepository(),
        customerRepository
    );
  }

  /*
   * Oauth 인증 실패 핸들러
   * */
  @Bean
  public OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler() {
    return new OAuth2AuthenticationFailureHandler(
        oAuth2AuthorizationRequestBasedOnCookieRepository());
  }

  /*
   * Cors 설정
   *
   */
  @Bean
  public UrlBasedCorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.addAllowedOriginPattern(
        "*"); // addAllowedOrigin("*")은 allowCredentials(true)랑 같이 사용 불가능
    configuration.addAllowedMethod("*");
    configuration.addAllowedHeader("*");

    configuration.setAllowCredentials(true);
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }

  @Bean
  public RoleHierarchy roleHierarchy() {

    RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
    Map<String, List<String>> roleHierarchyMap = new HashMap<>();

    roleHierarchyMap.put("ROLE_ADMIN", Arrays.asList("ROLE_USER"));

    String roles = RoleHierarchyUtils.roleHierarchyFromMap(roleHierarchyMap);
    roleHierarchy.setHierarchy(roles);
    return roleHierarchy;
  }

  @Bean
  public SecurityExpressionHandler<FilterInvocation> expressionHandler() {
    DefaultWebSecurityExpressionHandler webSecurityExpressionHandler =
        new DefaultWebSecurityExpressionHandler();
    webSecurityExpressionHandler.setRoleHierarchy(roleHierarchy());
    return webSecurityExpressionHandler;
  }

}
