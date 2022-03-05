package com.khumu.community.application.port.out.repository;

import com.khumu.community.application.entity.Article;
import com.khumu.community.application.entity.Board;
import com.khumu.community.application.entity.LikeArticle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LikeArticleRepository extends JpaRepository<LikeArticle, Integer> {
    Page<LikeArticle> findAllByUser(String user, Pageable pageable);

    List<LikeArticle> findAllByUserAndArticle(String user, Integer article);

    Long countByUserAndArticle(String user, Integer article);

    Long countByArticle(Integer article);


}
