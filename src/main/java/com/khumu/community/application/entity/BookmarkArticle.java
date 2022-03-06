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
    // Bookmark 정보를 통해 Article 정보를 조회하는 경우가 있기 때문에
    // PK 값 참조가 아닌 객체 참조를 한다.
    @ManyToOne
    @JoinColumn(name="article_id")
    Article article;
    // Bookmark 정보를 통해 User 정보를 조회하는 경우는 없기 때문에
    // 객체 참조가 아닌 간단한 PK 값 참조를 한다.
    @Column(name="user_id")
    String user;
}
