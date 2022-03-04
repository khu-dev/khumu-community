package com.khumu.community.application.port.out.repository;

import com.khumu.community.application.entity.Article;
import com.khumu.community.application.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, String> {
}
