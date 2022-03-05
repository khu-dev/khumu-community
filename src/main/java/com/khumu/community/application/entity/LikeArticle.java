package com.khumu.community.application.entity;

import com.khumu.community.infra.db.JpaConverterJson;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity(name="article_likearticle")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class LikeArticle extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    @Column(name="article_id")
    Integer article;
    @Column(name="user_id")
    String user;
}
