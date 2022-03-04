package com.khumu.community.infra.controller;

import com.khumu.community.application.dto.ArticleDto;
import com.khumu.community.application.dto.ReportDto;
import com.khumu.community.application.dto.input.CreateArticleRequest;
import com.khumu.community.application.dto.input.UpdateArticleRequest;
import com.khumu.community.application.entity.Article;
import com.khumu.community.application.entity.User;
import com.khumu.community.application.port.in.ArticleService;
import com.khumu.community.application.port.in.ReportService;
import com.khumu.community.infra.security.AuthDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ArticleController {
    final private ArticleService articleService;

    @GetMapping(value = "/api/community/v1/articles")
    @ResponseBody
    @ResponseStatus(code = HttpStatus.OK)
    public DefaultResponse<List<ArticleDto>> list(@AuthenticationPrincipal User user, @PageableDefault(page=0, size=20, sort="createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<ArticleDto> articles = articleService.listArticlesForFeed(user, pageable);
        return DefaultResponse.<List<ArticleDto>>builder()
                .data(articles.getContent())
                .links(DefaultResponse.Links.builder().build())
                .build();
    }

    @PostMapping(value = "/api/community/v1/articles")
    @ResponseBody
    @ResponseStatus(code = HttpStatus.CREATED)
    public DefaultResponse<ArticleDto> list(@AuthenticationPrincipal User user, @RequestBody CreateArticleRequest body) {
        ArticleDto article = articleService.write(user, body);
        return DefaultResponse.<ArticleDto>builder()
                .data(article)
                .links(DefaultResponse.Links.builder().build())
                .build();
    }

    @PatchMapping(value = "/api/community/v1/articles/{id}")
    @ResponseBody
    @ResponseStatus(code = HttpStatus.OK)
    public DefaultResponse<ArticleDto> list(@AuthenticationPrincipal User user, @PathVariable Integer id, @RequestBody UpdateArticleRequest body) {
        ArticleDto article = articleService.update(user, id, body);
        return DefaultResponse.<ArticleDto>builder()
                .data(article)
                .links(DefaultResponse.Links.builder().build())
                .build();
    }

    @DeleteMapping(value = "/api/community/v1/articles/{id}")
    @ResponseBody
    @ResponseStatus(code = HttpStatus.OK)
    public DefaultResponse<Void> delete(@AuthenticationPrincipal User user, @PathVariable Integer id) {
        articleService.delete(user, id);
        return DefaultResponse.<Void>builder()
                .data(null)
                .links(DefaultResponse.Links.builder().build())
                .build();
    }
}
