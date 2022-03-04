package com.khumu.community.application.port.in;

import com.khumu.community.application.dto.ReportDto;
import com.khumu.community.application.entity.Report;
import com.khumu.community.application.entity.User;
import com.khumu.community.application.port.out.noti.Notifier;
import com.khumu.community.application.port.out.repository.ReportRepository;
import com.khumu.community.common.mapper.ReportMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class ReportService {
    private final Notifier notifier;
    private final ReportRepository reportRepository;
    private final ReportMapper reportMapper;

    public ReportDto report(User requestUser, ReportDto input) {
        notifier.notify(input.getResourceKind() + "(id=" + input.getResourceId() + ")에 대한 신고가 접수되었어요!", input.getContent());
        Report report = reportRepository.save(reportMapper.toEntity(input));
        ReportDto output = reportMapper.toDto(report);

        return output;
    }
}
