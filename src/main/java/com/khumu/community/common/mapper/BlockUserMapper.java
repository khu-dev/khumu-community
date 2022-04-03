package com.khumu.community.common.mapper;

import com.khumu.community.application.dto.ArticleDto;
import com.khumu.community.application.dto.ArticleEventDto;
import com.khumu.community.application.dto.BlockUserDto;
import com.khumu.community.application.dto.DetailedArticleDto;
import com.khumu.community.application.entity.Article;
import com.khumu.community.application.entity.BlockUser;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses= {}
)
public interface BlockUserMapper {
    // blockee는 String -> SimpleUserDto로 수동으로 변환되어야함.
    @Mapping(target="blockee", ignore = true)
    BlockUserDto toDto(BlockUser src);
}