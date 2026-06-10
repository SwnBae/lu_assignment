package com.ludens.assignment.global.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ludens.assignment.global.common.ErrorResponse;
import com.ludens.assignment.global.exception.ErrorCode;
import com.ludens.assignment.global.security.JwtAuthenticationFilter;
import com.ludens.assignment.global.security.TokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final TokenProvider tokenProvider;
    private final ObjectMapper objectMapper;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, "/auth/signup", "/auth/login").permitAll()
                        .requestMatchers("/debug/**").permitAll()
                        .requestMatchers(HttpMethod.GET,
                                "/users/**",
                                "/posts/*/image",
                                "/search/posts",
                                "/recommendation"
                        ).permitAll()
                        .requestMatchers(HttpMethod.GET, "/posts/{id}").permitAll()
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .anyRequest().authenticated()
                )
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(this::handleUnauthorized)
                        .accessDeniedHandler(this::handleForbidden)
                )
                .addFilterBefore(
                        new JwtAuthenticationFilter(tokenProvider),
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }

    private void handleUnauthorized(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException e) throws IOException {
        writeJsonResponse(response, HttpServletResponse.SC_UNAUTHORIZED, ErrorCode.UNAUTHORIZED);
    }

    private void handleForbidden(HttpServletRequest request, HttpServletResponse response,
            AccessDeniedException e) throws IOException {
        writeJsonResponse(response, HttpServletResponse.SC_FORBIDDEN, ErrorCode.UNAUTHORIZED);
    }

    private void writeJsonResponse(HttpServletResponse response, int status, ErrorCode errorCode)
            throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(status);
        response.getWriter().write(
                objectMapper.writeValueAsString(new ErrorResponse(errorCode.getCode(), errorCode.getMessage()))
        );
    }
}
