package com.khumu.community.infra.security;

import com.khumu.community.application.exception.ExpiredTokenException;
import com.khumu.community.application.exception.InvalidTokenException;
import com.khumu.community.application.port.in.UserDetailServiceImpl;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Map;

@RequiredArgsConstructor
@Component
public class JwtTokenProvider {

    @Value("${khumu.jwt.secret}")
    private String secretKey;

    @Value("${khumu.jwt.access.expiryDuration}")
    private Long accessExpiryDuration;

    @Value("${khumu.jwt.refresh.expiryDuration}")
    private Long refreshExpiryDuration;

    private static final String HEADER = "Authorization";
    private static final String PREFIX = "Bearer";

    private final UserDetailServiceImpl authService;

    public String generateAccessToken(Map<String, String> claims) {
        return generateToken(claims, "access", accessExpiryDuration);
    }

//    public String generateRefreshToken(String email) {
//        return generateToken(email, "refresh", refreshExpiryDuration);
//    }

    private String generateToken(Map<String, String> claims, String type, Long expiryDuration) {
        // 여기 value들 한 번 점검해봐야할 듯
        return Jwts.builder()
                .setHeaderParam("typ", type)
                .setIssuedAt(new Date())
                .setClaims(claims)
                .setExpiration(new Date(System.currentTimeMillis() + expiryDuration * 1000 * 30))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

//    public boolean isRefreshToken(String token) {
//        try {
//            return getClaims(token).getHeader().get("typ").equals("refresh");
//        } catch (Exception e) {
//            throw new TokenTypeNotRefreshException();
//        }
//    }

    public String extractToken(HttpServletRequest request) {
        try {
            String bearerToken = request.getHeader(HEADER);
            if (bearerToken != null && bearerToken.startsWith(PREFIX)) {
                if (!validateToken(bearerToken.substring((PREFIX + " ").length()))) {
                    throw new ExpiredTokenException();
                }
                return bearerToken.substring((PREFIX + " ").length());
            }
            return null;
        } catch(ExpiredTokenException e) {
            e.printStackTrace();
            throw e;
        } catch(Exception e) {
            e.printStackTrace();
            throw new InvalidTokenException(e);
        }
    }

    public Authentication getAuthentication(String token) {
        AuthDetails auth = (AuthDetails) authService.loadUserByUsername(getUsername(token));
        // Authentication에 principal로 설정한 것이 @AuthenticationPrincipal에서 사용된다.
        return new UsernamePasswordAuthenticationToken(auth.getUser(), null, auth.getAuthorities());
    }

    private boolean validateToken(String token) {
        return getClaims(token).getBody().getExpiration().after(new Date());
    }

    private String getUsername(String token) {
        return getClaims(token).getBody().get("user_id", String.class);
    }

    private Jws<Claims> getClaims(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
    }

}