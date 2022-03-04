package com.khumu.community.application.entity;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity(name="user_khumuuser")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class User {
    @Id
    String username;
    String password;
    Boolean isActive;
    @CreatedDate
    LocalDateTime dateJoined;
    @CreatedDate
    LocalDateTime createdAt;
    String kind;
    String studentNumber;

    @Column(name="is_superuser")
    Boolean isSuperUser;
    String profileImage;

    @Column(name="info21_authenticated_at")
    LocalDateTime info21AuthenticatedAt;
    String status;
    String nickname;
}
