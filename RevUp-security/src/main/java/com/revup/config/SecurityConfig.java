package com.revup.config;

import com.revup.config.converter.RevUpAuthorizationCodeGrantRequestEntityConverter;
import com.revup.oauth.handler.Oauth2AuthenticationFailureHandler;
import com.revup.oauth.handler.Oauth2AuthenticationSuccessHandler;
import com.revup.oauth.repository.HttpCookieOauth2AuthorizationRequestRepository;
import com.revup.constants.SecurityUrlEndpoint;
import com.revup.jwt.filter.RevUpJwtFilter;
import com.revup.jwt.RevUpJwtProvider;
import com.revup.oauth.service.RevUpOAuth2UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.endpoint.DefaultAuthorizationCodeTokenResponseClient;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final RevUpJwtProvider jwtProvider;

    private final WebConfig webConfig;
    private final RevUpOAuth2UserService oAuth2UserService;
    private final Oauth2AuthenticationSuccessHandler oauth2AuthenticationSuccessHandler;
    private final Oauth2AuthenticationFailureHandler oauth2AuthenticationFailureHandler;
    private final HttpCookieOauth2AuthorizationRequestRepository httpCookieOauth2AuthorizationRequestRepository;

    private final RevUpAuthenticationEntryPoint authenticationEntryPoint;
    private final RevUpAccessDeniedHandler accessDeniedHandler;


    @Bean
    SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        DefaultAuthorizationCodeTokenResponseClient tokenResponseClient =
                new DefaultAuthorizationCodeTokenResponseClient();
        tokenResponseClient.setRequestEntityConverter(new RevUpAuthorizationCodeGrantRequestEntityConverter());
        httpSecurity
                // CSRF 비활성화: JWT를 사용할 경우 CSRF 공격을 방지할 필요가 없음
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(webConfig.corsConfigurationSource()))
                .headers(headersConfigurer -> headersConfigurer.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
                .formLogin(AbstractHttpConfigurer::disable)
                // httpBasic 비활성화
                .httpBasic(AbstractHttpConfigurer::disable)
                // 세션 관리 설정: Stateless
                .sessionManagement(
                        sessionManagement ->
                                sessionManagement.sessionCreationPolicy(
                                        SessionCreationPolicy.STATELESS))
                // 필터 추가
                .addFilterBefore(
                        new RevUpJwtFilter(jwtProvider),
                        UsernamePasswordAuthenticationFilter.class)

                // 접근 제어 설정
                .authorizeHttpRequests(
                        authorizationManagerRequestMatcherRegistry -> {
                            // permitAll() 설정하는 곳, 추가하려면 SecurityUrlEndpoint에서 추가.
                            for (SecurityUrlEndpoint securityUrlEndPoint :
                                    SecurityUrlEndpoint.values()) {
                                authorizationManagerRequestMatcherRegistry
                                        .requestMatchers(
                                                securityUrlEndPoint.getMethod(),
                                                securityUrlEndPoint.getUrl())
                                        .permitAll();
                            }
                            authorizationManagerRequestMatcherRegistry.anyRequest().authenticated();
                        })
                .oauth2Login(configure -> configure
                        .authorizationEndpoint(config -> config
                                .authorizationRequestRepository(httpCookieOauth2AuthorizationRequestRepository))
                        .userInfoEndpoint(config -> config.userService(oAuth2UserService))
                        .successHandler(oauth2AuthenticationSuccessHandler)
                        .failureHandler(oauth2AuthenticationFailureHandler));

        httpSecurity.exceptionHandling(
                httpSecurityExceptionHandlingConfigurer ->
                        httpSecurityExceptionHandlingConfigurer
                                // 토큰
                                .authenticationEntryPoint(authenticationEntryPoint)
                                .accessDeniedHandler(accessDeniedHandler));

        return httpSecurity.build();
    }

    // 특정 URI 필터 제외
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        // 요청 URL이 "/api/v1"로 시작하지 않으면 true, 그 외는 무시
        return web ->
                web.ignoring().requestMatchers(
                        "/geoserver/web/"
                );
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
