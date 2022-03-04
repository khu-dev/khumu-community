package com.khumu.community.application.dto;

import com.khumu.community.application.entity.Board;
import com.khumu.community.application.entity.User;
import com.khumu.community.common.util.JpaConverterJson;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ArticleDto implements Serializable {
    Integer id;
    String boardName;
    String boardDisplayName;
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
    LocalDateTime createdAt;
}
