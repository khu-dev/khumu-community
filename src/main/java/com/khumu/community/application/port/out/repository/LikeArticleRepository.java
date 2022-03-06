package com.khumu.community.application.port.out.repository;

import com.khumu.community.application.entity.Article;
import com.khumu.community.application.entity.Board;
import com.khumu.community.application.entity.LikeArticle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LikeArticleRepository extends JpaRepository<LikeArticle, Integer> {
    Page<LikeArticle> findAllByUserAndArticle_Author_UsernameNotIn(String user, List<String> excludedAuthorUsernames, Pageable pageable);

    List<LikeArticle> findAllByUserAndArticle(String user, Article article);

    Long countByUserAndArticle(String user, Article article);

    Long countByArticle(Article article);


}
