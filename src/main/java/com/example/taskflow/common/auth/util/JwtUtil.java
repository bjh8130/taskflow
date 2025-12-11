package com.example.taskflow.common.auth.util;

import com.example.taskflow.domain.user.enums.UserRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import java.util.Date;

/*
    JWT 생성 및 검증
 */
@Slf4j(topic = "JwtUtil")
@Component
public class JwtUtil {

    public static final String BEARER_PREFIX = "Bearer ";

    private static final long TOKEN_TIME = 60 * 60 * 1000L;

    @Value("YXNkZnNkZnNkZmxramVramtsYXNsa2xla25mbGFrc25sa2VmbmtsYW5zZmxrbmxlbmFzZmtsbmFzbGtuZWxhbmxrbmVsa2ZubGthc2Vm")           // application.yml 에 있는 key 가져옴
    private String secret;

    private SecretKey key;

    /*
     * Bean이 생성된 후 실행됨 - 문자열 secret을 암호화 키(SecretKey)로 변환하는 작업
     **/
    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    /*
    * JWT 토큰 생성 메서드
    * - 서버가 로그인한 사용자의 정보를 담아 클라이언트에게 전달
    * - 클라이언트는 이 토큰을 이후 요청의 신분증 처럼 사용
    * */
    public String generateToken(Long userId, String username, String email, UserRole role) {
        long now = System.currentTimeMillis();
        return Jwts.builder()
                .subject(String.valueOf(userId)) //JWT의 기본 subject — 일반적으로 유저의 고유 ID를 넣는다.
                .claim("username", username)
                .claim("email", email)
                .claim("role", role)
                .issuedAt(new Date(now))
                .expiration(new Date(now + TOKEN_TIME))
                .signWith(key) // secret key로 jwt 서명
                .compact(); // jwt 문자열 생성
    }

    /*
        Jwt claims(내용) 꺼내기
        클라이언트가 보낸 토큰에서 유저 정보등을 읽기 위함
     */
    public Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(key) // 토큰이 진짜인지 확인
                .build()
                .parseSignedClaims(token) // 서명 검증 + 파싱
                .getPayload(); // 안에 담긴 내용 claims
    }

    /*
        토큰이 유효한지 검사하는 메서드
        - 진짜인지, 만료되지는 않았는지
     */
    public boolean validate(String token) {
        try{
            getClaims(token); //파싱이 성공하면 정상 토큰
            return true;
        } catch(Exception e) { // 파싱 중 오류(만료, 위조) 유효하지 않은 토큰
            return false;
        }
    }

    public Long getUserId(String token) {
        return Long.valueOf(
                Jwts.parser()
                        .verifyWith(key)
                        .build()
                        .parseSignedClaims(token)
                        .getPayload()
                        .getSubject()
        );
    }

}
