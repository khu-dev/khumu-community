package com.khumu.community.application.port.out.repository;

import com.khumu.community.application.entity.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ArticleRepository extends JpaRepository<Article, Integer> {
    Page<Article> findAllByAuthor(User author, Pageable pageable);

    // 내가 팔로우 중인 Board의 게시글이며
    // 내가 차단하지 않은 작성자의 게시글
    // 을 위함.
    Page<Article> findAllByBoardInAndAuthor_UsernameNotIn(List<Board> boards, List<String> excludedAuthorUsernames, Pageable pageable);

    Page<Article> findAllByIsHotAndAuthor_UsernameNotIn(Boolean isHot, List<String> excludedAuthorUsernames, Pageable pageable);

    Page<Article> findAllByIdInAndAuthor_UsernameNotIn(List<Integer> ids, List<String> excludedAuthorUsernames, Pageable pageable);
    Page<Article> findAllByIdIn(List<Long> ids, Pageable pageable);

    Page<Article> findAllByIsHot(Boolean isHot, Pageable pageable);
}
