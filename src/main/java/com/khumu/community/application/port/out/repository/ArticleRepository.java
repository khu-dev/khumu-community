package com.khumu.community.application.port.out.repository;

import com.khumu.community.application.entity.Article;
import com.khumu.community.application.entity.Board;
import com.khumu.community.application.entity.Report;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArticleRepository extends JpaRepository<Article, Integer> {
    Page<Article> findAllByAuthor(String reporter, Pageable pageable);

    Page<Article> findAllByBoardIn(List<Board> boards, Pageable pageable);

    Page<Article> findAllByIdIn(List<Long> ids, Pageable pageable);

    Page<Article> findAllByIsHot(Boolean isHot, Pageable pageable);
}
