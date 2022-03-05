package com.khumu.community.application.entity;

import com.khumu.community.infra.db.JpaConverterJson;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity(name="article_article")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Article extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    String title;
    // 일반 String은 VARCHAR(255)로 정의왼다.
    @Column(columnDefinition = "TEXT")
    String content;
    @Convert(converter = JpaConverterJson.class)
    List<String> newImages;
    @ManyToOne
    @JoinColumn(name="author_id")
    User author;
    @ManyToOne
    @JoinColumn(name="board_id")
    Board board;
    String kind;
    @Column(name="is_hot")
    Boolean isHot;
}
