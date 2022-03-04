package com.khumu.community.application.entity;

import org.springframework.data.annotation.CreatedDate;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

public class ArticleBlock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    // 차단을 수행한 사람
    String blocker;
    // 차단한 게시글
    String article;
    @CreatedDate
    LocalDateTime createdAt;
}
