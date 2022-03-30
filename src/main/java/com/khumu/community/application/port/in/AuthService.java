package com.khumu.community.application.port.in;

import com.khumu.community.application.dto.LoginOutput;
import com.khumu.community.application.dto.input.LoginInput;
import com.khumu.community.application.entity.User;
import com.khumu.community.application.port.out.repository.UserRepository;
import com.khumu.community.infra.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Service
@Slf4j
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;

    public LoginOutput issueToken(LoginInput body) throws UsernameNotFoundException {
        User user = userRepository.findById(body.getUsername()).orElseThrow(WrongCredentialException::new);
        if (!passwordEncoder.matches(body.getPassword(), user.getPassword())) {
            throw new WrongCredentialException();
        }


        return LoginOutput.builder()
                .access(tokenProvider.generateAccessToken(generateClaims(user)))
                .build();
    }

    public static class WrongCredentialException extends RuntimeException {
        public WrongCredentialException() {
            super("일치하는 정보의 유저가 존재하지 않습니다.");
        }
    }

    private String generateFakeJTID() {
        // django에서 기본적으로 jti(jtid)를 통해 토큰의 고유 아이디를 정의한다.
        // 하지만 번거로워서 현재는 우선 fake로만 구현.
        return "fake_jtid";
    }

    private Map<String, String> generateClaims(User user) {
        Map<String, String> claims = new HashMap<>();
        claims.put("user_id", user.getUsername());
        claims.put("nickname", user.getNickname());
        claims.put("kind", user.getKind());
        claims.put("token_type", "access");
        claims.put("jti", generateFakeJTID());

        return claims;
    }

}
