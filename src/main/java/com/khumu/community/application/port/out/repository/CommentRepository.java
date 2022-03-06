package com.khumu.community.application.port.out.repository;

import com.khumu.community.application.entity.BookmarkArticle;
import com.khumu.community.application.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository {
    Long countByArticle(Integer article);

    // 내가 댓글 단 게시글 조회를 위해
    // 댓글을 달았던 게시글의 아이디를 comment MSA에게 질의한다.
    // page number와 page size만 동작한다.
    // 어떤 유저에 관해 조회할 지를 설정하기 위해 authorizationString(JWT)를 인자로 받는다.
    List<Integer> findAllArticlesUserCommented(String authorizationString, Pageable pageable);
}
