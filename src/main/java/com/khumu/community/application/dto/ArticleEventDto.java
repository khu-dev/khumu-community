package com.khumu.community.application.dto;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ArticleEventDto implements BaseEventDto {
    Integer id;
    String title;
    String content;
    List<String> newImages;
    String author;
    String board;
    String kind;
    Boolean isHot;
}
