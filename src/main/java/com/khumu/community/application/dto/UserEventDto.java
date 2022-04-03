package com.khumu.community.application.dto;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.Column;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.List;

/*
SNS에 event가 publish될 때 기존에 django가 전송하던 json 형태
{
    "created_at": "2022-03-09T17:08:13.185",
    "date_joined": "2022-03-09T17:08:13.185",
    "department": "\ud559\uacfc \ubbf8\uc124\uc815",
    "groups": [],
    "info21_authenticated_at": null,
    "is_active": true,
    "is_superuser": false,
    "kind": "guest",
    "last_login": null,
    "nickname": "\uc9c4\uc218a",
    "password": "pbkdf2_sha256$216000$T3WrferGl8GC$TKpWdClQOIEGJh3c6jcBKiflHK0VmDXF2K2q4+FaPTY=",
    "profile_image": null,
    "status": "exists",
    "student_number": "2000123123",
    "user_permissions": [],
    "username": "jinsua"
}
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserEventDto implements BaseEventDto {
    String username;
    Boolean isActive;
    LocalDateTime dateJoined;
    LocalDateTime createdAt;
    String kind;
    String studentNumber;
    Boolean isSuperUser;
    String profileImage;
    LocalDateTime info21AuthenticatedAt;
    String status;
    String nickname;
}
