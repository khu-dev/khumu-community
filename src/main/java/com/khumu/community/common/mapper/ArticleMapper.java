package com.khumu.community.common.mapper;

import com.khumu.community.application.dto.ArticleDto;
import com.khumu.community.application.dto.ArticleEventDto;
import com.khumu.community.application.dto.DetailedArticleDto;
import com.khumu.community.application.dto.ReportDto;
import com.khumu.community.application.entity.Article;
import com.khumu.community.application.entity.Report;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses= {ArticleMapper.class}
)
public interface ArticleMapper {
    @Mapping(target="boardName", source="board.name")
    @Mapping(target="boardDisplayName", source="board.displayName")
    @Mapping(target="createdAt", qualifiedByName="localDateTimeToHumanFriendlyString")
    ArticleDto toDto(Article src);

    @Mapping(target="boardName", source="board.name")
    @Mapping(target="boardDisplayName", source="board.displayName")
    @Mapping(target="createdAt", qualifiedByName="localDateTimeToHumanFriendlyString")
    DetailedArticleDto toDetailedDto(Article src);

    Article toEntity(ArticleDto src);

    @Mapping(target="author", source = "author.username")
    @Mapping(target="board", source = "board.name")
    ArticleEventDto toEventDto(Article src);

    @Named("localDateTimeToHumanFriendlyString")
    static String localDateTimeToHumanFriendlyString(LocalDateTime t) {
        LocalDateTime now = LocalDateTime.now();
        if (t.until(now, ChronoUnit.MINUTES) < 5) {
            return "지금";
        } else if (t.until(now, ChronoUnit.MINUTES) < 60) {
            return t.until(now, ChronoUnit.MINUTES) + "분 전";
        } else if (t.getYear() == now.getYear() && t.getMonth() == now.getMonth() && t.getDayOfMonth() ==now.getDayOfMonth()) {
            return String.format("%02d:%02d", t.getHour(), t.getMinute());
        } else if (t.getYear() == now.getYear()){
            return String.format("%02d/%02d %02d:%02d", t.getMonthValue(), t.getDayOfMonth(), t.getHour(), t.getMinute());
        }

        return String.format("%d/%02d/%02d %02d:%02d", t.getYear(), t.getMonthValue(), t.getDayOfMonth(), t.getHour(), t.getMinute());
    }
}