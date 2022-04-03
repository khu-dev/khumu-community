package com.khumu.community.application.dto.input;

import lombok.*;

// 게시글의 작성자를 차단한다.
// 게시글이 익명 게시글이라면 차단 후 정보 제공 시 익명으로 정보를 제공하고
// 기명 게시글이라면 기명으로 정보 제공
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CreateBlockUserInput {
    Integer article;
}
