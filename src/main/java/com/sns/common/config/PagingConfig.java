package com.sns.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.config.PageableHandlerMethodArgumentResolverCustomizer;

@Configuration
public class PagingConfig {

    // 클라이언트가 /posts?page=1이라고 보내면 스프링 내부적으로 page=0으로 바꿔줌
    @Bean
    public PageableHandlerMethodArgumentResolverCustomizer customizePageable() {
        return resolver -> resolver.setOneIndexedParameters(true);
    }

}
