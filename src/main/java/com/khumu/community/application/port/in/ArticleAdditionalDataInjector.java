package com.khumu.community.application.port.in;

import com.khumu.community.application.dto.ArticleDto;
import com.khumu.community.application.dto.DetailedArticleDto;
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

    public ArticleDto inject(ArticleDto articleDto, User requestUser) {
        articleDto.setCommentCount(commentRepository.countByArticle(articleDto.getId()));
        if (requestUser != null){
            articleDto.setLiked(likeArticleRepository.countByUserAndArticle(requestUser.getUsername(), Article.builder().id(articleDto.getId()).build()) != 0);
        }
        articleDto.setLikeArticleCount(likeArticleRepository.countByArticle(Article.builder().id(articleDto.getId()).build()));

        if (requestUser != null) {
            articleDto.setBookmarked(bookmarkArticleRepository.countByUserAndArticle(requestUser.getUsername(), Article.builder().id(articleDto.getId()).build()) != 0);
        }

        articleDto.setBookmarkArticleCount(bookmarkArticleRepository.countByArticle(Article.builder().id(articleDto.getId()).build()));

        return articleDto;
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

        if (requestUser == null || requestUser.getUsername() == null) {
            src.setIsSubscribed(false);

            return src;
        }

        src.setIsSubscribed(alimiRepository.isSubscribed(requestUser.getUsername(), "article", String.valueOf(src.getId())));

        return src;
    }
}
