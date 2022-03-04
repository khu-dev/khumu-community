package com.khumu.community.common.mapper;

import com.khumu.community.application.dto.ArticleDto;
import com.khumu.community.application.dto.SimpleUserDto;
import com.khumu.community.application.entity.Article;
import com.khumu.community.application.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses= {}
)
public interface UserMapper {
    @Mapping(target="status", constant="exists")
    SimpleUserDto toSimpleDto(User src);
}