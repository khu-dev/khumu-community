package com.khumu.community.application.entity;

import com.khumu.community.application.exception.UnauthenticatedException;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity(name="user_khumuuser")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class User extends BaseEntity{
    @Id
    String username;
    String password;
    Boolean isActive;
    @CreatedDate
    @Builder.Default
    LocalDateTime dateJoined = LocalDateTime.now();
    String kind;
    String studentNumber;

    @Column(name="is_superuser")
    Boolean isSuperUser;
    String profileImage;

    @Column(name="info21_authenticated_at")
    LocalDateTime info21AuthenticatedAt;
    String status;
    String nickname;

    @ManyToMany
    @JoinTable(name = "user_khumuuser_groups",
            joinColumns = @JoinColumn(name = "khumuuser_id"),
            inverseJoinColumns = @JoinColumn(name = "group_id"))
            @Builder.Default
    List<Group> groups = new ArrayList<>();

    public static void throwOnUnauthenticated(User user) {
        if (user == null || user.getUsername() == null) {
            throw new UnauthenticatedException("인증되지 않은 사용자입니다.");
        }
    }
}
