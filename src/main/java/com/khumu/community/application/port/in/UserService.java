package com.khumu.community.application.port.in;

import com.khumu.community.application.dto.UserDto;
import com.khumu.community.application.dto.input.CreateUserInput;
import com.khumu.community.application.entity.Group;
import com.khumu.community.application.entity.User;
import com.khumu.community.application.exception.BaseKhumuException;
import com.khumu.community.application.port.out.messaging.MessagePublisher;
import com.khumu.community.application.port.out.repository.UserRepository;
import com.khumu.community.common.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@RequiredArgsConstructor
@Service
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final List<String> cuteAdjectives = List.of("행복한", "열정의", "귀여운", "매력넘치는");
    private final Random random = new Random(System.currentTimeMillis());
    private final MessagePublisher messagePublisher;

    @Transactional
    public UserDto create(CreateUserInput input) {
        if (input.getKind() == null) {
            input.setKind("guest");
            log.info("user kind가 null이므로 기본값인 guest로 설정합니다.");
        }

        User tmp = null;
        switch (input.getKind()){
            case "guest":
                tmp = createGuestUser(input);
                break;
            case "student":
                log.error("아직 경희대 가입은 지원하지 않아요.");
                throw new BaseKhumuException("아직 경희대 가입은 지원하지 않아요.");
            default:
                log.error("지원하지 않는 유저 유형이에요.");
                throw new BaseKhumuException("지원하지 않는 유저 유형이에요.");
        }

        User user = userRepository.save(tmp);
        messagePublisher.publish("user", "create", userMapper.toEventDto(user));

        return userMapper.toDto(user);
    }

    private User createGuestUser(CreateUserInput input) {
        String randomNickname = cuteAdjectives.get(random.nextInt(cuteAdjectives.size())) + "게스트#" + random.nextInt(9999) + 1;

        return User.builder()
                .username(randomNickname)
                .password(input.getPassword())
                .nickname(randomNickname)
                .kind("guest")
                .studentNumber("XXXXXXXXXX")
                .status("exists")
                .isSuperUser(false)
                .isActive(true)
                .groups(List.of(Group.builder().id(1).build()))
                .build();
    }
}
