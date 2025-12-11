package com.example.taskflow.common.auth.util;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import java.util.Date;

@Slf4j(topic = "JwtUtil")
@Component
public class JwtUtil {

    public static final String BEARER_PREFIX = "Bearer ";

    private static final long TOKEN_TIME = 60 * 60 * 1000L;

    @Value("${jwt.secret.key}")           // application.yml 에 있는 key 가져옴
    private String secretKeyString;

    private SecretKey key;
    private JwtParser parser;

    // 기본세팅값
    @PostConstruct
    public void init() {
        byte[] bytes = Decoders.BASE64.decode(secretKeyString);
        this.key = Keys.hmacShaKeyFor(bytes);
        this.parser = Jwts.parser()
                .verifyWith(this.key)
                .build();
    }

    // 토큰 생성
    public String login(String username) {
        Date now = new Date();
        return BEARER_PREFIX + Jwts.builder()
                .claim("username", username)
                .issuedAt(now)
                .expiration(new Date(now.getTime() + TOKEN_TIME))
                .signWith(key, Jwts.SIG.HS256)
                .compact();
    }
    // 토큰 검증
    // 토큰이 비어있는지 검사해주고, 비어있거나 null이면 유효하지 않음을 던져준다.
    // 만약 null이 아니고 비어있지 않고 제대로된 내용이 있다면 요 내용을 유효한지 유효하지 않은지 검사
    // parseSignedClaims로 유효성 체크
    public boolean validateToken(String token) {
        if (token == null || token.isBlank()) return false;
        try {
            parser.parseSignedClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            // 개별 예외 분리 없음: 서명/형식/만료 등 모든 실패를 한 번에 처리
            log.debug("Invalid JWT: {}", e.toString());
            return false;
        }
    }

}
