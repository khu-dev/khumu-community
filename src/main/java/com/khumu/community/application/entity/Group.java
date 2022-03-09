package com.khumu.community.application.entity;

import com.khumu.community.application.exception.UnauthenticatedException;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity(name="auth_group")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Group extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    String name;
}
