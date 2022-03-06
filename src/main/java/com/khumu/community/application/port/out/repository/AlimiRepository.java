package com.khumu.community.application.port.out.repository;

import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AlimiRepository {
    Boolean isSubscribed(String username, String resourceKind, String resourceId);
}
