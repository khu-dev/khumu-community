package com.khumu.community.application.entity;

import com.khumu.community.infra.db.JpaConverterJson;
import com.vladmihalcea.hibernate.type.json.JsonStringType;
import lombok.*;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity(name="article_article")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@TypeDef(name = "json", typeClass = JsonStringType.class)
public class Article extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    String title;
    // 일반 String은 VARCHAR(255)로 정의왼다.
    // LONGTEXT는 VARCHAR가 아닌 Long text로 저장됨.
    @Column(columnDefinition = "LONGTEXT")
    String content;

    @Type( type = "json" )
    @Column( columnDefinition = "json")
    @Builder.Default
    // https://stackoverflow.com/questions/44308167/how-to-map-a-mysql-json-column-to-a-java-entity-property-using-jpa-and-hibernate
    List<String> images = new ArrayList<>();
    // 이런 식으로 List<String> 를 표현하려하면 단순 LONGTEXT 타입 컬럼이 되어버림.
    // JPA는 MySQL의 json 컬럼으로는 못 쓰고 Text column을 json 처럼 파싱해서 쓸 수 밖에 없나?
//    @Convert(converter = JpaConverterJson.class)
//    @Column(columnDefinition = "LONGTEXT")
//    @Builder.Default
//    List<String> newImages = new ArrayList<>();
    
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
    @Builder.Default
    @Enumerated(value = EnumType.STRING)
    Status status = Status.EXISTS;
}
