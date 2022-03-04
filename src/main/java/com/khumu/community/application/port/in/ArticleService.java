package com.khumu.community.application.port.in;

import com.khumu.community.application.dto.ArticleDto;
import com.khumu.community.application.entity.Article;
import com.khumu.community.application.entity.Board;
import com.khumu.community.application.entity.FollowBoard;
import com.khumu.community.application.entity.User;
import com.khumu.community.application.port.out.repository.ArticleRepository;
import com.khumu.community.application.port.out.repository.FollowBoardRepository;
import com.khumu.community.common.mapper.ArticleMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class ArticleService {
    private final ArticleMapper articleMapper;
    private final ArticleRepository articleRepository;
    private final FollowBoardRepository followBoardRepository;

    public Page<ArticleDto> listArticlesForFeed(User requestUser, Pageable pageable) {
        List<Board> boards = followBoardRepository.findAllByUser(requestUser, Pageable.unpaged()).map(FollowBoard::getBoard).toList();
        Page<Article> articles = articleRepository.findAllByBoardIn(
               boards,
                pageable
        );

//        Page<Article> a = articleRepository.findAll(Pageable.unpaged());

        return articles.map(articleMapper::toDto);
    }
}
