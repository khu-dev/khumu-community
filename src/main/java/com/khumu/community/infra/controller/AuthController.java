package com.khumu.community.infra.controller;

import com.khumu.community.application.dto.BlockUserDto;
import com.khumu.community.application.dto.LoginOutput;
import com.khumu.community.application.dto.input.CreateBlockUserInput;
import com.khumu.community.application.dto.input.LoginInput;
import com.khumu.community.application.entity.User;
import com.khumu.community.application.port.in.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AuthController {
    final private AuthService authService;

    @PostMapping(value = "/api/community/v1/login")
    @ResponseBody
    @ResponseStatus(code = HttpStatus.OK)
    public LoginOutput issueToken(@RequestBody LoginInput body) {
        return authService.issueToken(body);
    }
}
