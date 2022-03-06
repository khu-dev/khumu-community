package com.khumu.community.application.dto.output;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class IsAuthorOutput {
    @Builder.Default
    Boolean isAuthor = false;
}
