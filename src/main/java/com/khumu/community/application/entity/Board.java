package com.khumu.community.application.entity;

import com.khumu.community.common.util.JpaConverterJson;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

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
