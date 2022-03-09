package com.khumu.community.infra.controller;

import com.khumu.community.application.dto.ReportDto;
import com.khumu.community.application.entity.User;
import com.khumu.community.application.port.in.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ReportController {
    final private ReportService reportService;

    @PostMapping(value = "/api/community/v1/reports")
    @ResponseBody
    @ResponseStatus(code = HttpStatus.CREATED)
    public DefaultResponse<ReportDto> report(@AuthenticationPrincipal User user, @RequestBody ReportDto reportDto) {
        return DefaultResponse.<ReportDto>builder()
                .data(reportService.report(user, reportDto))
                .build();
    }
}
