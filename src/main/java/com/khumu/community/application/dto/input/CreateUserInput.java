package com.khumu.community.application.dto.input;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
// TODO: 현재는 guest나 dev 유저만 지원
// student kind의 유저는 추후에 info21과 연동해서 생각해보자.
public class CreateUserInput {
    String username;
    String password;
    String nickname;
    String kind;
}
