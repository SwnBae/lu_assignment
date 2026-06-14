package com.ludens.assignment.global.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ludens.assignment.global.filter.RateLimitFilter;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

import javax.sql.DataSource;

@Configuration
public class RateLimitConfig {

    @Bean
    public FilterRegistrationBean<RateLimitFilter> rateLimitFilter(
            @Value("${server.tomcat.threads.max:200}") int maxThreads,
            @Value("${server.tomcat.accept-count:100}") int acceptCount,
            DataSource dataSource,
            ObjectMapper objectMapper) {

        HikariDataSource hikari = (HikariDataSource) dataSource;
        int threshold = maxThreads + acceptCount - hikari.getMaximumPoolSize();

        FilterRegistrationBean<RateLimitFilter> bean = new FilterRegistrationBean<>(
                new RateLimitFilter(hikari, objectMapper, threshold)
        );
        bean.addUrlPatterns("/*");
        bean.setOrder(Ordered.HIGHEST_PRECEDENCE + 1);
        return bean;
    }
}
