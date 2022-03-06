package com.khumu.community.application.port.out.repository;

import com.khumu.community.application.entity.Board;
import com.khumu.community.application.entity.FollowBoard;
import com.khumu.community.application.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowBoardRepository extends JpaRepository<FollowBoard, Long> {
    Page<FollowBoard> findAllByUser(String username, Pageable pageable);
}
