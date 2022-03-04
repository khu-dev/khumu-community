package com.khumu.community.common.mapper;

import com.khumu.community.application.dto.ReportDto;
import com.khumu.community.application.entity.Report;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses= {}
)
public interface ReportMapper {
    ReportDto toDto(Report src);
    Report toEntity(ReportDto src);
}