package com.khumu.community.application.port.in;

import com.khumu.community.application.dto.ArticleDto;
import com.khumu.community.application.dto.input.CreateArticleRequest;
import com.khumu.community.application.dto.input.UpdateArticleRequest;
import com.khumu.community.application.entity.Article;
import com.khumu.community.application.entity.Board;
import com.khumu.community.application.entity.FollowBoard;
import com.khumu.community.application.entity.User;
import com.khumu.community.application.exception.ForbiddenException;
import com.khumu.community.application.port.out.repository.ArticleRepository;
import com.khumu.community.application.port.out.repository.FollowBoardRepository;
import com.khumu.community.common.mapper.ArticleMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class ArticleService {
    private final ArticleMapper articleMapper;
    private final ArticleRepository articleRepository;
    private final FollowBoardRepository followBoardRepository;

    @Transactional
    public ArticleDto write(User requestUser, CreateArticleRequest input) {
        Article tmp = Article.builder()
                .author(requestUser)
                .board(Board.builder().name(input.getBoard()).build())
                .title(input.getTitle())
                .content(input.getContent())
                .isHot(false)
                .newImages(input.getImages())
                .kind(input.getKind())
                .build();

        Article article = articleRepository.save(tmp);

        // TODO: 게시글 생성 이벤트를 SNS에 발행하기!
        return articleMapper.toDto(article);
    }

    @Transactional
    public Page<ArticleDto> listArticlesForFeed(User requestUser, Pageable pageable) {
        List<Board> boards = followBoardRepository.findAllByUser(requestUser, Pageable.unpaged()).map(FollowBoard::getBoard).toList();
        Page<Article> articles = articleRepository.findAllByBoardIn(
               boards,
                pageable
        );

//        Page<Article> a = articleRepository.findAll(Pageable.unpaged());

        return articles.map(articleMapper::toDto);
    }

    @Transactional
    // TODO: 기존에는 Patch(부분 수정) 방식이었는데 이거 어떻게 대응해줄 것인가?
    // 일단은 null일 수 없는 값들이기때문에 null이면 수정 안하기로.
    public ArticleDto update(User requestUser, Integer id, UpdateArticleRequest input) {

        Article article = articleRepository.findById(id).get();
        if (input.getBoard() != null) {
            article.setBoard(Board.builder().name(input.getBoard()).build());
        }

        if (input.getTitle() != null) {
            article.setTitle(input.getTitle());
        }
        if (input.getContent() != null) {
            article.setContent(input.getContent());
        }
        if (input.getImages() != null) {
            article.setNewImages(input.getImages());
        }
        if (input.getKind() != null) {
            article.setKind(input.getKind());
        }

        return articleMapper.toDto(article);
    }

    @Transactional
    public void delete(User requestUser, Integer id) {
        Article article = articleRepository.findById(id).get();
        try {
            if (!article.getAuthor().getUsername().equals(requestUser.getUsername())) {
                throw new ForbiddenException("해당 게시글의 작성자가 아닙니다.");
            }
        } catch (NullPointerException e) {
            throw new ForbiddenException("해당 게시글의 작성자가 아닙니다.");
        }
        articleRepository.delete(article);
    }
}
