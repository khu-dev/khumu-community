package com.khumu.community.infra.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.Filter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtTokenProvider tokenProvider;
    final List<String> SKIPPER_URIS = Arrays.asList("/", "/api/community/v1/login");

//    public Jws<Claims> getVerifiedJwsFromToken(String token) {
//        Jws<Claims> jws = jwtUtil.parseToken(token);
//        return jws;
//    }
//    public String getUserIdFromJws(Jws<Claims> jws) {
//        return jws.getBody().get("user_id", String.class);
//    }
//
//    public List<GrantedAuthority> getAuthoritiesFromJws(Jws<Claims> jws) {
//        return new ArrayList<>();
//    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (SKIPPER_URIS.stream().anyMatch(skip -> skip.equals(request.getRequestURI()))) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            if (request.getHeader("Authorization") != null && request.getHeader("Authorization").startsWith("Bearer ")) {
                String token = tokenProvider.extractToken(request);
                Authentication authentication = tokenProvider.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(
                        authentication);
            } else{
                // token string이 null인 경우. 미인증 상태로 진행.
                log.info("Authorization Bearer Token 내에 올바른 유저 정보 없음. 미인증 상태로 요청 진행");
            }
        } catch (Exception e) {
            // token을 verify하다 실패하면 unauthenticated 상태로 계속함.
            e.printStackTrace();
            log.info("Authorization Bearer Token verify 도중 오류 발생. 미인증 상태로 요청 진행");
        }

        filterChain.doFilter(request, response);
    }
}
