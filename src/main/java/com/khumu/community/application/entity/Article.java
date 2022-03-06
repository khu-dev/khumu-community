package com.khumu.community.application.entity;

import com.khumu.community.infra.db.JpaConverterJson;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity(name="article_article")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Article extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    String title;
    // 일반 String은 VARCHAR(255)로 정의왼다.
    @Column(columnDefinition = "TEXT")
    String content;
    @Convert(converter = JpaConverterJson.class)
    @Builder.Default
    List<String> newImages = new ArrayList<>();
    
    // 게시글을 조회할 때는 항상 작성자 정보도 필요하기 때문에
    // 객체 참조를 한다.
    @ManyToOne
    @JoinColumn(name="author_id")
    User author;
    
    // 게시글을 조회할 때는 항상 게시판 정보도 필요하기 때문에
    // 객체 참조를 한다. 
    @ManyToOne
    @JoinColumn(name="board_id")
    Board board;
    String kind;
    @Column(name="is_hot")
    Boolean isHot;
}
