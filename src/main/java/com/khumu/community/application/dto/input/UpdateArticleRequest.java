package com.khumu.community.application.dto.input;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UpdateArticleRequest {
    String title;
    String board;
    String content;
    String kind;
    List<String> images;
}
