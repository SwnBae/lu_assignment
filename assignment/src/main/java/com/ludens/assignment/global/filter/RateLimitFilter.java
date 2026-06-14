package com.ludens.assignment.global.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ludens.assignment.global.common.ErrorResponse;
import com.ludens.assignment.global.exception.ErrorCode;
import com.zaxxer.hikari.HikariDataSource;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class RateLimitFilter extends OncePerRequestFilter {

    private final HikariDataSource hikariDataSource;
    private final ObjectMapper objectMapper;
    private final int threshold;

    public RateLimitFilter(HikariDataSource hikariDataSource, ObjectMapper objectMapper, int threshold) {
        this.hikariDataSource = hikariDataSource;
        this.objectMapper = objectMapper;
        this.threshold = threshold;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {
        int pending = hikariDataSource.getHikariPoolMXBean().getThreadsAwaitingConnection();
        if (pending > threshold) {
            response.setContentType("application/json;charset=UTF-8");
            response.setStatus(429);
            response.getWriter().write(
                    objectMapper.writeValueAsString(
                            new ErrorResponse(
                                    ErrorCode.TOO_MANY_REQUESTS.getCode(),
                                    ErrorCode.TOO_MANY_REQUESTS.getMessage()
                            )
                    )
            );
            return;
        }
        chain.doFilter(request, response);
    }
}
