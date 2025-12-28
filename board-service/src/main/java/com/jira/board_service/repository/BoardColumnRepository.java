package com.jira.board_service.repository;

import com.jira.board_service.entity.BoardColumn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BoardColumnRepository extends JpaRepository<BoardColumn, UUID> {
    List<BoardColumn> findByBoardIdOrderByPosition(UUID boardId);

    boolean existsByBoardIdAndNameIgnoreCase(UUID boardId, String name);

    @Modifying
    @Query("""
            UPDATE BoardColumn c
            SET c.position = c.position + 1
            WHERE c.board.id = :boardId
            AND c.position BETWEEN :start AND :end
            """)
    void incrementPositions(UUID boardId, int start, int end);

    @Modifying
    @Query("""
            UPDATE BoardColumn c
            SET c.position = c.position - 1
            WHERE c.board.id = :boardId
            AND c.position BETWEEN :start AND :end
            """)
    void decrementPositions(UUID boardId, int start, int end);
}
