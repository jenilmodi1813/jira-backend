package com.jira.board_service.repository;

import com.jira.board_service.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface BoardRepository extends JpaRepository<Board, UUID> {
    Optional<Board> findByProjectId(UUID projectId);
    boolean existsByProjectId(UUID projectId);
}
