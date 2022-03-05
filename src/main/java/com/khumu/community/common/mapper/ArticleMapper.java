package com.khumu.community.common.mapper;

import com.khumu.community.application.dto.ArticleDto;
import com.khumu.community.application.dto.ArticleEventDto;
import com.khumu.community.application.dto.ReportDto;
import com.khumu.community.application.entity.Article;
import com.khumu.community.application.entity.Report;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses= {ArticleMapper.class}
)
public interface ArticleMapper {
    @Mapping(target="boardName", source="board.name")
    @Mapping(target="boardDisplayName", source="board.displayName")
    // TODO
    // 나중엔 newImages -> images로 컬럼 명 자체를 바꿔야할 듯
    @Mapping(target="images", source="newImages")
    ArticleDto toDto(Article src);
    Article toEntity(ArticleDto src);
    @Mapping(target="author", source = "author.username")
    @Mapping(target="board", source = "board.name")
    ArticleEventDto toEventDto(Article src);
}