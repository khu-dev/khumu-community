package com.khumu.community.application.dto;

import com.khumu.community.application.entity.Board;
import com.khumu.community.application.entity.User;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class SimpleUserDto implements Serializable {
    String username;
    String nickname;
    String status;
}
