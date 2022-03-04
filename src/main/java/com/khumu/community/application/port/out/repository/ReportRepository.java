package com.khumu.community.application.port.out.repository;

import com.khumu.community.application.entity.Report;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<Report, Long> {
    Page<Report> findAllByReporter(String reporter, Pageable pageable);

    Page<Report> findAllByResourceKindAndResourceId(String resourceKind, String resourceId, Pageable pageable);
}
