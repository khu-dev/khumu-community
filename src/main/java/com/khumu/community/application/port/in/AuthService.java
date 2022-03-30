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

        Map<String, String> claims = new HashMap<>();
        claims.put("user_id", user.getUsername());
        claims.put("nickname", user.getNickname());
        claims.put("kind", user.getKind());
        return LoginOutput.builder()
                .access(tokenProvider.generateAccessToken(claims))
                .build();
    }

    public static class WrongCredentialException extends RuntimeException {
        public WrongCredentialException() {
            super("일치하는 정보의 유저가 존재하지 않습니다.");
        }
    }

}
