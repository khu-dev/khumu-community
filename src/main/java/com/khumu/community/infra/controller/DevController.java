package com.khumu.community.infra.controller;

import com.khumu.community.application.port.out.repository.ArticleRepository;
import com.khumu.community.infra.messaging.SnsPublisher;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class DevController {
    private final ArticleRepository articleRepository;
    private final SnsPublisher snsNotificationSender;

    @GetMapping(value = "/healthz")
    @ResponseBody
    @ResponseStatus(code = HttpStatus.OK)
    public void healthCheck() {
        // 개발 해볼 내용 정의
    }

    @PostMapping(value = "/api/community/v1/dummy")
    @ResponseBody
    @ResponseStatus(code = HttpStatus.OK)
    public void dummy() {
        // 개발 해볼 내용 정의
    }
}
