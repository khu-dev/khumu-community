package com.khumu.community.application.dto;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class BlockUserDto {
    Long id;
    // 차단을 수행한 사람
    String blocker;
    // 차단 당한 사람
    SimpleUserDto blockee;
    // 차단 당한 사람의 정보를 익명으로 가려줄 것인지
    Boolean isBlockeeAnonymous;
    // 차단한 이유
    String reason;
    // 언제 차단한 것인지
    LocalDateTime createdAt;
}
