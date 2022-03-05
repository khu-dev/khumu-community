package com.khumu.community.application.entity;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity(name="board_board")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Board {
    @Id
    String name;
    String displayName;
    String description;
//    String campus;
    String category;
//    String adminId;
//    String relatedDepartmentId;
//    String relatedLectureSuiteId;
//    String relatedSubjectId;
    @CreatedDate
    LocalDateTime createdAt;
}
