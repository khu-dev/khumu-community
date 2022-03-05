package com.khumu.community.application.port.out.repository;

import com.khumu.community.application.entity.BookmarkArticle;
import com.khumu.community.application.entity.LikeArticle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookmarkArticleRepository extends JpaRepository<BookmarkArticle, Integer> {
    Page<BookmarkArticle> findAllByUser(String user, Pageable pageable);

    List<BookmarkArticle> findAllByUserAndArticle(String user, Integer article);
    Long countByUserAndArticle(String user, Integer article);

    Long countByArticle(Integer article);
}
