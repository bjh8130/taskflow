package com.example.taskflow.common.auth.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                // 프론트엔드 도메인
                .allowedOrigins("http://localhost:3000")
                // 허용할 HTTP 메서드
                .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
                // 프론트에서 보내는 헤더 허용
                .allowedHeaders("*")
                // 서버에서 노출할 헤더 (프론트가 받을 수 있는 응답 헤더)
                .exposedHeaders("Authorization")
                // 인증 정보 허용 (JWT, 쿠키 등)
                .allowCredentials(true)
                // Preflight 요청 결과 캐싱 (초 단위)
                .maxAge(3600);
    }
}

