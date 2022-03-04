package com.khumu.community.common.mapper;

import com.khumu.community.application.dto.ArticleDto;
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
//    @Mapping(target="images", source="")
    ArticleDto toDto(Article src);
    Article toEntity(ArticleDto src);
}