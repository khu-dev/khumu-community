package com.khumu.community.infra.controller;

import com.khumu.community.application.dto.ArticleDto;
import com.khumu.community.application.dto.input.CreateArticleRequest;
import com.khumu.community.application.dto.input.UpdateArticleRequest;
import com.khumu.community.application.entity.User;
import com.khumu.community.application.port.in.ArticleService;
import com.khumu.community.application.port.in.BookmarkArticleService;
import com.khumu.community.application.port.in.LikeArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ArticleController {
    final private ArticleService articleService;
    final private LikeArticleService likeArticleService;
    final private BookmarkArticleService bookmarkArticleService;
    
    @GetMapping(value = "/api/community/v1/articles")
    @ResponseBody
    @ResponseStatus(code = HttpStatus.OK)
    public DefaultResponse<List<ArticleDto>> list(
            @AuthenticationPrincipal User user,
            @RequestHeader(name="Authorization") String authorizationString,
            @RequestParam String board,
            @PageableDefault(page=0, size=20, sort="createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<ArticleDto> articles = null;
        switch (board) {
            case "following":
            case "feed":
                articles = articleService.listArticlesForFeed(user, pageable);
                break;
            case "my":
                articles = articleService.listArticlesIWrote(user, pageable);
                break;
            case "liked":
                articles = articleService.listArticlesILiked(user, pageable);
                break;
            case "bookmarked":
                articles = articleService.listArticlesIBookmarked(user, pageable);
                break;
            case "commented":
                return DefaultResponse.<List<ArticleDto>>builder()
                        .data(articleService.listArticlesICommented(user, authorizationString, pageable))
                        .build();
            default:
                articles = articleService.listArticlesByBoard(user, board, pageable);
        }

        return DefaultResponse.<List<ArticleDto>>builder()
                .data(articles.getContent())
                .build();
    }

    @PostMapping(value = "/api/community/v1/articles")
    @ResponseBody
    @ResponseStatus(code = HttpStatus.CREATED)
    public DefaultResponse<ArticleDto> create(@AuthenticationPrincipal User user, @RequestBody CreateArticleRequest body) {
        ArticleDto article = articleService.write(user, body);
        return DefaultResponse.<ArticleDto>builder()
                .data(article)
                .build();
    }

    @PatchMapping(value = "/api/community/v1/articles/{id}")
    @ResponseBody
    @ResponseStatus(code = HttpStatus.OK)
    public DefaultResponse<ArticleDto> update(
            @AuthenticationPrincipal User user,
            @PathVariable Integer id,
            @RequestBody UpdateArticleRequest body) {
        ArticleDto article = articleService.update(user, id, body);
        return DefaultResponse.<ArticleDto>builder()
                .data(article)
                .build();
    }

    @DeleteMapping(value = "/api/community/v1/articles/{id}")
    @ResponseBody
    @ResponseStatus(code = HttpStatus.OK)
    public DefaultResponse<Void> delete(@AuthenticationPrincipal User user, @PathVariable Integer id) {
        articleService.delete(user, id);
        return DefaultResponse.<Void>builder()
                .data(null)
                .build();
    }

    @PatchMapping(value = "/api/community/v1/articles/{id}/likes")
    @ResponseBody
    public ResponseEntity<DefaultResponse<Object>> toggleLike(@AuthenticationPrincipal User user, @PathVariable Integer id) {
        Boolean didLike = likeArticleService.toggle(user, id);
        if (didLike) {
            return ResponseEntity.status(HttpStatus.CREATED).body(DefaultResponse.builder().message("게시글을 좋아요했습니다.").build());
        }

        // 근데 204면 No content라 body가 아예 안가긴하네.
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(DefaultResponse.builder().message("게시글에 대한 좋아요를 취소했습니다.").build());
    }

    @PatchMapping(value = "/api/community/v1/articles/{id}/bookmarks")
    @ResponseBody
    public ResponseEntity<DefaultResponse<Object>> toggleBookmark(@AuthenticationPrincipal User user, @PathVariable Integer id) {
        Boolean didBookmark = bookmarkArticleService.toggle(user, id);
        if (didBookmark) {
            return ResponseEntity.status(HttpStatus.CREATED).body(DefaultResponse.builder().message("게시글을 북마크했습니다.").build());
        }

        // 근데 204면 No content라 body가 아예 안가긴하네.
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(DefaultResponse.builder().message("게시글에 대한 북마크를 취소했습니다.").build());
    }
}
