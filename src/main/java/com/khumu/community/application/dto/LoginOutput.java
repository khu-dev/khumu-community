package com.khumu.community.application.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class LoginOutput {
    String access;
    // TODO
    // 아직 refresh logic은 없음
//    String refresh;
}
