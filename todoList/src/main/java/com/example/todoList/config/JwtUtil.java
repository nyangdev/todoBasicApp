package com.example.todoList.config;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    private final Key key;

    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 15; // 15분
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 1000L * 60 * 60 * 24 * 7; // 7일

    public JwtUtil(@Value("${jwt.secret}") String secretKey) {
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    // Access Token 생성
    public String createAccessToken(String username) {
        return createToken(username, ACCESS_TOKEN_EXPIRE_TIME);
    }

    // Refresh Token 생성
    public String createRefreshToken(String username) {
        return createToken(username, REFRESH_TOKEN_EXPIRE_TIME);
    }

    // 공통 토큰 생성 로직
    private String createToken(String username, long expireTime) {
        return Jwts.builder()
                .setSubject(username) // username을 subject로 저장
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expireTime))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // 토큰에서 username 추출
    public String extractUsername(String token) {
        return parseClaims(token).getSubject();
    }

    // 토큰 유효성 검사
    public boolean isValidToken(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    // 토큰 만료 여부 확인
    public boolean isTokenExpired(String token) {
        try {
            Date exp = parseClaims(token).getExpiration();
            return exp.before(new Date());
        } catch (Exception e) {
            return true;
        }
    }

    // 내부 Claims 파싱
    private Claims parseClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(this.key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
