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
    // 좋아요는 좋아요를 리스트해서 Article 정보를 받아올 때가 있기 때문에
    // PK 참조가 아닌 객체 참조를 한다.
    @ManyToOne
    @JoinColumn(name="article_id")
    Article article;

    // 좋아요 정보를 통해 User 정보를 조회하는 경우는 없기 때문에
    // 객체 참조가 아닌 간단한 PK 값 참조를 한다.
    @Column(name="user_id")
    String user;
}
