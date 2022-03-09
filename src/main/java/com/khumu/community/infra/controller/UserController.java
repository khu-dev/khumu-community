package com.khumu.community.infra.controller;

import com.khumu.community.application.dto.ReportDto;
import com.khumu.community.application.dto.UserDto;
import com.khumu.community.application.dto.input.CreateUserInput;
import com.khumu.community.application.entity.User;
import com.khumu.community.application.port.in.ReportService;
import com.khumu.community.application.port.in.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UserController {
    final private UserService userService;

    @PostMapping(value = "/api/community/v1/users")
    @ResponseBody
    @ResponseStatus(code = HttpStatus.CREATED)
    public DefaultResponse<UserDto> createUser(@RequestBody CreateUserInput body) {
        return DefaultResponse.<UserDto>builder()
                .data(userService.create(body))
                .build();
    }
}
