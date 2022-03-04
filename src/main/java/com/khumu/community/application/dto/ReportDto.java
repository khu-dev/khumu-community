package com.khumu.community.application.dto;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ReportDto{
    Long id;
    String reporter;
    String resourceKind;
    String resourceId;
    // 신고 내용
    String content;
    LocalDateTime createdAt;
}
