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
    @ManyToOne
    @JoinColumn(name="board_id")
    Board board;
    @ManyToOne
    @JoinColumn(name="user_id")
    User user;
    @CreatedDate
    LocalDateTime followedAt;
    @CreatedDate
    LocalDateTime createdAt;
}
