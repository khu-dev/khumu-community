package com.khumu.community.application.port.in;

import com.khumu.community.application.dto.ArticleDto;
import com.khumu.community.application.dto.input.UpdateArticleRequest;
import com.khumu.community.application.entity.*;
import com.khumu.community.application.exception.ForbiddenException;
import com.khumu.community.application.port.out.messaging.MessagePublisher;
import com.khumu.community.application.port.out.repository.ArticleRepository;
import com.khumu.community.application.port.out.repository.LikeArticleRepository;
import com.khumu.community.common.mapper.ArticleMapper;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class LikeArticleService {
    private final ArticleMapper articleMapper;
    private final LikeArticleRepository likeArticleRepository;
    private final ArticleRepository articleRepository;
    private final MessagePublisher messagePublisher;
    @Value("${khumu.community.minLikeCountForHotArticle}")
    private Long minLikeCountForHotArticle;

    @Transactional
    // 좋아요 했으면 true
    // 좋아요를 취소했으면 false
    public Boolean toggle(User requestUser, Integer articleId) {
        List<LikeArticle> likes = likeArticleRepository.findAllByUserAndArticle(requestUser.getUsername(), articleId);

        if (likes.isEmpty()) {
            likeArticleRepository.save(LikeArticle.builder()
                    .user(requestUser.getUsername())
                    .article(articleId)
                    .build());

            Article article = articleRepository.findById(articleId).get();
            if (shouldBeHot(article)) {
                messagePublisher.publish("article", "new_hot_article", articleMapper.toEventDto(article));
                article.setIsHot(true);
            }

            return true;
        }

        likeArticleRepository.deleteAll(likes);


        return false;
    }

    // 게시글이 아직 핫 게시글이 아닌가?
    // 최소 개수 이상의 좋아요를 받았는가?
    // -> 그렇다면 핫 게시글이 되어야한다.
    private Boolean shouldBeHot(Article article) {
        if (article.getIsHot()) return false;

        Long likeCount = likeArticleRepository.countByArticle(article.getId());
        if (likeCount < minLikeCountForHotArticle) {
            return false;
        }

        return true;
    }
}
