package com.khumu.community.application.port.out.repository;

import com.khumu.community.application.entity.BlockUser;
import com.khumu.community.application.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BlockUserRepository extends JpaRepository<BlockUser, Long> {
    Page<BlockUser> findAllByBlocker(String blocker, Pageable pageable);
}
