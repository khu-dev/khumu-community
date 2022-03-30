package com.khumu.community.common.mapper;

import com.khumu.community.application.dto.ArticleDto;
import com.khumu.community.application.dto.SimpleUserDto;
import com.khumu.community.application.dto.UserDto;
import com.khumu.community.application.dto.UserEventDto;
import com.khumu.community.application.entity.Article;
import com.khumu.community.application.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.stream.Collectors;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses= {},
        imports={Collectors.class}
)
public interface UserMapper {
    // TODO: 이거 status deleted일 때도 생각해야하는데
    // 영컨의 User가 아니라 간이로 만든 유저가 전달되는 경우도 있음.
    @Mapping(target="status", constant="exists")
    SimpleUserDto toSimpleDto(User src);

    @Mapping(expression = "java( src.getGroups().stream().map(group -> group.getId()).collect(Collectors.toList()) )", target="groups")
    UserDto toDto(User src);

    UserEventDto toEventDto(User src);

}