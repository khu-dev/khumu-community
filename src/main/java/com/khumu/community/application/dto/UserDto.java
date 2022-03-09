package com.khumu.community.application.dto;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserDto {
    String username;
    String kind;
    String nickname;
    String studentNumber;
    String department;
    String status;
    @Builder.Default
    List<Integer> groups = new ArrayList<>();
    String createdAt;
}
