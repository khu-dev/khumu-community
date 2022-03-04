package com.khumu.community.application.port.in;

import com.khumu.community.application.dto.ArticleDto;
import com.khumu.community.application.dto.JwtDto;
import com.khumu.community.application.dto.SimpleUserDto;
import com.khumu.community.application.entity.Article;
import com.khumu.community.application.entity.Board;
import com.khumu.community.application.entity.FollowBoard;
import com.khumu.community.application.entity.User;
import com.khumu.community.application.port.out.repository.ArticleRepository;
import com.khumu.community.application.port.out.repository.FollowBoardRepository;
import com.khumu.community.application.port.out.repository.UserRepository;
import com.khumu.community.common.mapper.ArticleMapper;
import com.khumu.community.common.mapper.UserMapper;
import com.khumu.community.infra.security.AuthDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class AuthService implements UserDetailsService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
//    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return new AuthDetails(userRepository.findById(username).get());
    }
}
