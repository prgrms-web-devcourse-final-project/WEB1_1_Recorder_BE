package com.revup.config;

import com.revup.error.AppException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

import static com.revup.error.ErrorCode.UNKNOWN_EXCEPTION;

@Component
public class RevUpAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final HandlerExceptionResolver resolver;

    public RevUpAuthenticationEntryPoint(
            @Qualifier("handlerExceptionResolver") HandlerExceptionResolver resolver) {
        this.resolver = resolver;
    }

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException)
            throws IOException, ServletException {
        System.out.println("JwtAuthenticationEntryPoint JwtAuthenticationEntryPoint");
        if (isExceptionInSecurityFilter(request)) {
            resolver.resolveException(
                    request, response, null, (Exception) request.getAttribute("exception"));
            return;
        }
        resolver.resolveException(request, response, null, new AppException(UNKNOWN_EXCEPTION));
    }

    private boolean isExceptionInSecurityFilter(HttpServletRequest request) {
        return request.getAttribute("exception") != null;
    }
}