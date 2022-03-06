package com.khumu.community.application.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ArticleDto {
    Integer id;
    String boardName;
    String boardDisplayName;
    // TODO: 익명의 경우 작성자를 숨기기
    SimpleUserDto author;
    String title;
    String content;
    @Builder.Default
    List<String> images = new ArrayList<>();

    @Builder.Default
    Long commentCount = 0L;

    @Builder.Default
    Boolean liked = false;
    @Builder.Default
    Long likeArticleCount = 0L;

    @Builder.Default
    Boolean bookmarked = false;
    @Builder.Default
    Long bookmarkArticleCount = 0L;

    String kind;
    Boolean isHot;
    
    String createdAt;
}
