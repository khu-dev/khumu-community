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
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    // 신고를 수행한 사람
    String reporter;
    // 신고한 게시글 유형
    String resourceKind;
    // 신고한 게시글의 PK
    String resourceId;
    // 신고 내용
    String content;
    @CreatedDate
    LocalDateTime createdAt;
}
