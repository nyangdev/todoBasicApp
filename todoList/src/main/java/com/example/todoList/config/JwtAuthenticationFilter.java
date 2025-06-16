package com.example.todoList.config;

import com.example.todoList.entity.User;
import com.example.todoList.exception.ErrorResponse;
import com.example.todoList.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

// 매요청마다 헤더를 읽고 JWT 토큰이 유효한 경우 Spring Security의 인증 정보로 설정해주는 역할
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    public JwtAuthenticationFilter(JwtUtil jwtUtil, UserRepository userRepository) {
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            if (jwtUtil.isValidToken(token) && !jwtUtil.isTokenExpired(token)) {
                String username = jwtUtil.extractUsername(token);

                User user = userRepository.findByUsername(username).orElse(null);
                if (user != null) {
                    UsernamePasswordAuthenticationToken auth =
                            new UsernamePasswordAuthenticationToken(user, null, Collections.emptyList());
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            }

            else {
                // ErrorResponse 객체 생성
                ErrorResponse errorResponse = new ErrorResponse(
                        HttpStatus.UNAUTHORIZED.value(),
                        "Invalid Token",
                        null // errors 리스트가 따로 없다면 null 또는 Collections.emptyList() 사용
                );

                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json");

                ObjectMapper mapper = new ObjectMapper();
                mapper.registerModule(new JavaTimeModule());
                mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

                String json = mapper.writeValueAsString(errorResponse);
                response.getWriter().write(json);
                return; // 필터 중단
            }
        }

        filterChain.doFilter(request, response);
    }
}
