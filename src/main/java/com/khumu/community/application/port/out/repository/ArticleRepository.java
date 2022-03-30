package com.khumu.community.application.port.out.repository;

import com.khumu.community.application.entity.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ArticleRepository extends JpaRepository<Article, Integer> {
    Page<Article> findAllByAuthorAndStatusIn(User author, List<Status> statuses, Pageable pageable);

    // 내가 팔로우 중인 Board의 게시글이며
    // 내가 차단하지 않은 작성자의 게시글
    // 을 위함.
    Page<Article> findAllByBoardInAndAuthor_UsernameNotInAndStatusIn(List<Board> boards, List<String> excludedAuthorUsernames, List<Status> statuses, Pageable pageable);

    Page<Article> findAllByIsHotAndAuthor_UsernameNotInAndStatusIn(Boolean isHot, List<String> excludedAuthorUsernames, List<Status> statuses, Pageable pageable);

    Page<Article> findAllByIdInAndAuthor_UsernameNotInAndStatusIn(List<Integer> ids, List<String> excludedAuthorUsernames, List<Status> statuses, Pageable pageable);

    // 뭐할 때 쓰는 거지
    Page<Article> findAllByIdIn(List<Long> ids, Pageable pageable);
}
