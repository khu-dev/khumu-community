package com.khumu.community.infra.controller;

import com.khumu.community.application.dto.ArticleDto;
import com.khumu.community.application.dto.ReportDto;
import com.khumu.community.application.entity.Article;
import com.khumu.community.application.entity.User;
import com.khumu.community.application.port.in.ArticleService;
import com.khumu.community.application.port.in.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ArticleController {
    final private ArticleService articleService;

    @GetMapping(value = "/api/community/v1/articles")
    @ResponseBody
    @ResponseStatus(code = HttpStatus.OK)
    public DefaultResponse<List<ArticleDto>> list(@RequestParam String user, @PageableDefault(page=0, size=20, sort="createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<ArticleDto> articles = articleService.listArticlesForFeed(User.builder().username(user).build(), pageable);
        return DefaultResponse.<List<ArticleDto>>builder()
                .data(articles.getContent())
                .links(DefaultResponse.Links.builder().build())
                .build();
    }
}
