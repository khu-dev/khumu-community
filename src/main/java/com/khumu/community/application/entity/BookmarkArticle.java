package com.khumu.community.application.entity;

import lombok.*;

import javax.persistence.*;

@Entity(name="article_bookmarkarticle")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class BookmarkArticle extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    @Column(name="article_id")
    Integer article;
    @Column(name="user_id")
    String user;
}
