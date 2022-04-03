package com.khumu.community.application.dto.input;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class LoginInput {
    private String username;
    private String password;
    private Boolean createGuest;
}
