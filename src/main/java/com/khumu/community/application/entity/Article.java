package com.khumu.community.application.entity;

import com.fasterxml.jackson.annotation.JsonRawValue;
import com.khumu.community.common.util.JpaConverterJson;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity(name="article_article")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    String title;
    // 일반 String은 VARCHAR(255)로 정의왼다.
    @Column(columnDefinition = "TEXT")
    String content;
    @Convert(converter = JpaConverterJson.class)
    @Column(name="new_images")
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
    @CreatedDate
    LocalDateTime createdAt;
}
