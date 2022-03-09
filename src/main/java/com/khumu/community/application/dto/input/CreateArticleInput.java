package com.khumu.community.application.dto.input;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CreateArticleInput {
    String title;
    String board;
    String content;
    String kind;
    @Builder.Default
    List<String> images = new ArrayList<>();
}
