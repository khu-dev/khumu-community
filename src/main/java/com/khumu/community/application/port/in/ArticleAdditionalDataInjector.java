package com.khumu.community.application.port.in;

import com.khumu.community.application.dto.ArticleDto;
import com.khumu.community.application.dto.DetailedArticleDto;
import com.khumu.community.application.dto.SimpleUserDto;
import com.khumu.community.application.entity.Article;
import com.khumu.community.application.entity.BookmarkArticle;
import com.khumu.community.application.entity.LikeArticle;
import com.khumu.community.application.entity.User;
import com.khumu.community.application.port.out.repository.AlimiRepository;
import com.khumu.community.application.port.out.repository.BookmarkArticleRepository;
import com.khumu.community.application.port.out.repository.CommentRepository;
import com.khumu.community.application.port.out.repository.LikeArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;

// ArticleDto에 대한 추가 정보를 주입해준다.
@Component
@RequiredArgsConstructor
public class ArticleAdditionalDataInjector {
    private final LikeArticleRepository likeArticleRepository;
    private final BookmarkArticleRepository bookmarkArticleRepository;
    private final CommentRepository commentRepository;
    private final AlimiRepository alimiRepository;

    public ArticleDto inject(ArticleDto src, User requestUser) {
        src.setCommentCount(commentRepository.countByArticle(src.getId()));
        if (requestUser != null && requestUser.getUsername() != null){
            src.setLiked(likeArticleRepository.countByUserAndArticle(requestUser.getUsername(), Article.builder().id(src.getId()).build()) != 0);
        }
        src.setLikeArticleCount(likeArticleRepository.countByArticle(Article.builder().id(src.getId()).build()));

        if (requestUser != null && requestUser.getUsername() != null) {
            src.setBookmarked(bookmarkArticleRepository.countByUserAndArticle(requestUser.getUsername(), Article.builder().id(src.getId()).build()) != 0);
        }

        src.setBookmarkArticleCount(bookmarkArticleRepository.countByArticle(Article.builder().id(src.getId()).build()));
        if (src.getKind().equals("anonymous")) {
            src.setAuthor(SimpleUserDto.builder().username("anonymous").nickname("익명").status("anonymous").build());
        }


        return src;
    }

    public Page<ArticleDto> inject(Page<ArticleDto> articleDtos, User requestUser) {
        for (ArticleDto articleDto :
                articleDtos) {
            inject(articleDto, requestUser);
        }

        return articleDtos;
    }
    public List<ArticleDto> inject(List<ArticleDto> articleDtos, User requestUser) {
        for (ArticleDto articleDto :
                articleDtos) {
            inject(articleDto, requestUser);
        }

        return articleDtos;
    }

    public DetailedArticleDto inject(DetailedArticleDto src, User requestUser) {
        // ArticleDto에 대한 inject와 동일
        src.setCommentCount(commentRepository.countByArticle(src.getId()));
        if (requestUser != null && requestUser.getUsername() != null){
            src.setLiked(likeArticleRepository.countByUserAndArticle(requestUser.getUsername(), Article.builder().id(src.getId()).build()) != 0);
        }
        src.setLikeArticleCount(likeArticleRepository.countByArticle(Article.builder().id(src.getId()).build()));

        if (requestUser != null && requestUser.getUsername() != null) {
            src.setBookmarked(bookmarkArticleRepository.countByUserAndArticle(requestUser.getUsername(), Article.builder().id(src.getId()).build()) != 0);
        }

        src.setBookmarkArticleCount(bookmarkArticleRepository.countByArticle(Article.builder().id(src.getId()).build()));
        if (src.getKind().equals("anonymous")) {
            src.setAuthor(SimpleUserDto.builder().username("anonymous").nickname("익명").status("anonymous").build());
        }

        if (requestUser == null || requestUser.getUsername() == null) {
            src.setIsSubscribed(false);

            return src;
        }

        src.setIsSubscribed(alimiRepository.isSubscribed(requestUser.getUsername(), "article", String.valueOf(src.getId())));

        return src;
    }
}
