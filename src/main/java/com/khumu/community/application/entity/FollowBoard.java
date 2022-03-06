package com.khumu.community.application.entity;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity(name="board_followboard")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class FollowBoard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    // Follow 정보를 통해 Board 정보를 조회하는 경우가 있기 때문에
    // PK 값 참조가 아닌 객체 참조를 한다.
    @ManyToOne
    @JoinColumn(name="board_id")
    Board board;

    // Follow 정보를 통해 User 정보를 조회하는 경우는 없기 때문에
    // 객체 참조가 아닌 간단한 PK 값 참조를 한다.
    @Column(name="user_id")
    String user;
    @CreatedDate
    LocalDateTime followedAt;
    @CreatedDate
    LocalDateTime createdAt;
}
