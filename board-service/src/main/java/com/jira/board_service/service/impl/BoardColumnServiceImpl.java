package com.jira.board_service.service.impl;

import com.jira.board_service.dto.request.UpdateBoardRequest;
import com.jira.board_service.dto.request.column.CreateColumnRequest;
import com.jira.board_service.dto.request.column.UpdateColumnRequest;
import com.jira.board_service.dto.response.BoardColumnResponse;
import com.jira.board_service.entity.Board;
import com.jira.board_service.entity.BoardColumn;
import com.jira.board_service.exception.BadRequestException;
import com.jira.board_service.exception.ResourceNotFoundException;
import com.jira.board_service.repository.BoardColumnRepository;
import com.jira.board_service.repository.BoardRepository;
import com.jira.board_service.service.BoardColumnService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class BoardColumnServiceImpl implements BoardColumnService {

    private final BoardRepository boardRepo;
    private final BoardColumnRepository columnRepo;

    public BoardColumnServiceImpl(BoardRepository boardRepo,
                                  BoardColumnRepository columnRepo) {
        this.boardRepo = boardRepo;
        this.columnRepo = columnRepo;
    }

    @Override
    public BoardColumnResponse create(UUID boardId, CreateColumnRequest req) {

        Board board = boardRepo.findById(boardId)
                .orElseThrow(() -> new ResourceNotFoundException("Board not found"));

        if (columnRepo.existsByBoardIdAndNameIgnoreCase(boardId, req.name())) {
            throw new BadRequestException("Column already exists");
        }

        BoardColumn column = BoardColumn.builder()
                .board(board)
                .name(req.name())
                .position(req.position())
                .isDone(req.isDone())
                .build();

        return map(columnRepo.save(column));
    }

    @Override
    public List<BoardColumnResponse> getByBoard(UUID boardId) {
        return columnRepo.findByBoardIdOrderByPosition(boardId)
                .stream()
                .map(this::map)
                .toList();
    }

    @Override
    public BoardColumnResponse update(UUID columnId, UpdateColumnRequest req) {

        BoardColumn column = columnRepo.findById(columnId)
                .orElseThrow(() -> new ResourceNotFoundException("Column not found"));

        column.setName(req.name());
        column.setPosition(req.position());
        column.setIsDone(req.isDone());

        return map(columnRepo.save(column));
    }

    @Override
    public void delete(UUID columnId) {
        if (!columnRepo.existsById(columnId)) {
            throw new ResourceNotFoundException("Column not found");
        }
        columnRepo.deleteById(columnId);
    }

    public void reorder(UUID boardId, UUID columnId, Integer newPosition) {

        BoardColumn column = columnRepo.findById(columnId)
                .orElseThrow(() -> new ResourceNotFoundException("Column not found"));

        int oldPosition = column.getPosition();

        if (oldPosition == newPosition) return;

        if (newPosition < oldPosition) {
            // moving UP
            columnRepo.incrementPositions(boardId, newPosition, oldPosition - 1);
        } else {
            // moving DOWN
            columnRepo.decrementPositions(boardId, oldPosition + 1, newPosition);
        }

        column.setPosition(newPosition);
        columnRepo.save(column);
    }

    @Override
    public com.jira.board_service.dto.response.column.BoardColumnResponse getById(UUID columnId) {

        BoardColumn column = columnRepo.findById(columnId)
                .orElseThrow(() -> new ResourceNotFoundException("Column not found"));

        return clientMap(column);
    }

    private BoardColumnResponse map(BoardColumn c) {
        return new BoardColumnResponse(
                c.getId(),
                c.getName(),
                c.getPosition(),
                c.getIsDone()
        );
    }

    private com.jira.board_service.dto.response.column.BoardColumnResponse clientMap(BoardColumn c) {
        return new com.jira.board_service.dto.response.column.BoardColumnResponse(
                c.getId(),
                c.getName(),
                c.getPosition(),
                c.getIsDone(),
                c.getBoard().getId()
        );
    }
}
