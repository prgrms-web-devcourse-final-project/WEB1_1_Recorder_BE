package com.revup.jwt.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revup.auth.dto.token.AccessToken;
import com.revup.auth.service.TokenValidator;
import com.revup.constants.SecurityConstants;
import com.revup.error.AppException;
import com.revup.error.SecurityException;
import com.revup.exception.UnsupportedTokenException;
import com.revup.jwt.RevUpJwtProvider;
import com.revup.auth.dto.token.TokenInfo;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

import static com.revup.error.ErrorCode.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class RevUpJwtFilter extends OncePerRequestFilter {

    private final RevUpJwtProvider jwtProvider;
    private final TokenValidator jwtValidator;

    private static final String REFRESH_URL = "/api/v1/auth/refresh";
    private static final String LOGOUT_URL = "/api/v1/auth/logout";

    @Override
    protected void doFilterInternal(
            HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String requestUrl = request.getServletPath();
            log.info("requestUrl = {}", requestUrl);
            if("/favicon.ico".equals(requestUrl)) return;

            HttpMethod requestMethod = HttpMethod.valueOf(request.getMethod());
            String tokenValue = extractToken(request);
            String tokenType = jwtProvider.getTokenType(tokenValue);

            switch (requestUrl) {
                case REFRESH_URL -> handleRefreshUrl(tokenValue, requestMethod);
                case LOGOUT_URL -> handleLogoutUrl(tokenValue, requestMethod);
                default -> handleOthersUrl(new AccessToken(tokenValue), tokenType);
            }

        } catch (SecurityException e) {
            log.error("Application Exception: {}", e.getErrorCode(), e);
            setErrorResponse(
                    response,
                    e
            );
            return;
        } catch (Exception e) {
            log.error("Unexpected Exception: {}", e.getMessage(), e);
            setErrorResponse(
                    response,
                    new SecurityException(UNKNOWN_EXCEPTION)
            );
            return;
        }

        log.debug("다음으로 이동");
        filterChain.doFilter(request, response);
    }

    private void handleLogoutUrl(String tokenValue, HttpMethod requestMethod) {
        if(!requestMethod.equals(HttpMethod.GET)) {
            throw new AppException(REQUEST_INVALID);
        }


    }

    private void handleRefreshUrl(String tokenValue, HttpMethod requestMethod) {
        if(!requestMethod.equals(HttpMethod.POST)) {
            throw new AppException(REQUEST_INVALID);
        }

        TokenInfo tokenInfo = extractTokenInfo(tokenValue);
        setSecurityContextHolder(tokenInfo);
    }

    private void handleOthersUrl(AccessToken token, String tokenType) {
        if(!tokenType.equals("ACCESS")) throw UnsupportedTokenException.EXCEPTION;
        jwtValidator.validate(token);
        TokenInfo tokenInfo = extractTokenInfo(token.value());
        setSecurityContextHolder(tokenInfo);
    }

    private TokenInfo extractTokenInfo(String token) {
        log.debug("token = {}", token);
        TokenInfo tokenUserPrincipal = jwtProvider.getTokenUserPrincipal(token);
        log.info("tokenUserPrincipal = {}", tokenUserPrincipal);
        return tokenUserPrincipal;
    }

    // 헤더에서 토큰 추출
    private String extractToken(HttpServletRequest httpServletRequest) {
        String bearerToken = httpServletRequest.getHeader(SecurityConstants.AUTHORIZATION_HEADER);

        if (StringUtils.hasText(bearerToken) &&
                bearerToken.startsWith(SecurityConstants.BEARER) &&
                bearerToken.length() > SecurityConstants.BEARER.length()
        ) {
                return bearerToken.substring(SecurityConstants.BEARER.length());
            }

        // 토큰 정보 칸이 비어있으면 없는 토큰으로 간주하고 오류 발생
        throw new AppException(TOKEN_NOT_EXIST);
    }

    private void setSecurityContextHolder(
            TokenInfo userPrincipal) {
        //권한 없이 설정
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(userPrincipal, null, List.of());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }

    private void setErrorResponse(HttpServletResponse response, SecurityException e) {
        response.setStatus(e.getErrorCode().getHttpStatus().value());
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        try {

            response.getWriter().write(new ObjectMapper().writeValueAsString(e));
        } catch (IOException ioException) {
            log.error("Error writing response: {}", ioException.getMessage());
        }
    }
}
