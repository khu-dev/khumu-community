package com.khumu.community.application.entity;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class BlockUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    // 차단을 수행한 사람
    String blocker;
    // 차단 당한 사람
    String blockee;
    // 차단 당한 사람의 정보를 익명으로 가려줄 것인지
    Boolean hidBlockee;
    // 언제 차단한 것인지
    @CreatedDate
    LocalDateTime createdAt;
}
