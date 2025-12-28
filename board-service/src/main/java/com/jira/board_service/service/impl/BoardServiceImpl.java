package com.jira.board_service.service.impl;

import com.jira.board_service.constance.BoardType;
import com.jira.board_service.constance.DefaultBoardColumns;
import com.jira.board_service.dto.request.CreateBoardRequest;
import com.jira.board_service.dto.request.UpdateBoardRequest;
import com.jira.board_service.dto.response.BoardColumnResponse;
import com.jira.board_service.dto.response.BoardResponse;
import com.jira.board_service.entity.Board;
import com.jira.board_service.entity.BoardColumn;
import com.jira.board_service.exception.BadRequestException;
import com.jira.board_service.exception.ResourceNotFoundException;
import com.jira.board_service.repository.BoardColumnRepository;
import com.jira.board_service.repository.BoardRepository;
import com.jira.board_service.service.BoardService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepo;
    private final BoardColumnRepository columnRepository;

    public BoardServiceImpl(BoardRepository boardRepo,BoardColumnRepository boardColumnRepository) {
        this.boardRepo = boardRepo;
        this.columnRepository = boardColumnRepository;
    }

    @Override
    public BoardResponse create(CreateBoardRequest req, UUID userId) {

        if (boardRepo.existsByProjectId(req.projectId())) {
            throw new BadRequestException("Board already exists for this project");
        }

        Board board = Board.builder()
                .id(UUID.randomUUID())
                .projectId(req.projectId())
                .name(req.name())
                .type(BoardType.valueOf(req.type()))
                .build();
        boardRepo.save(board);

        //  CREATE DEFAULT COLUMNS
        List<BoardColumn> columns = DefaultBoardColumns.DEFAULT_COLUMNS.stream()
                .map(def -> BoardColumn.builder()
                        .id(UUID.randomUUID())
                        .board(board)
                        .name(def.name())
                        .position(def.position())
                        .isDone(def.isDone())
                        .build())
                .toList();

        columnRepository.saveAll(columns);

        return map(board);
    }

    @Override
    public BoardResponse getByProject(UUID projectId) {
        Board board = boardRepo.findByProjectId(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Board not found"));
        return map(board);
    }

    @Override
    public BoardResponse update(UUID boardId, UpdateBoardRequest req) {
        Board board = boardRepo.findById(boardId)
                .orElseThrow(() -> new ResourceNotFoundException("Board not found"));

        board.setName(req.name());
        return map(boardRepo.save(board));
    }

    @Override
    public void delete(UUID boardId) {
        if (!boardRepo.existsById(boardId)) {
            throw new ResourceNotFoundException("Board not found");
        }
        boardRepo.deleteById(boardId);
    }


    private BoardResponse map(Board board) {
        List<BoardColumnResponse> columns =
                board.getColumns() == null ? List.of() :
                        board.getColumns().stream()
                                .map(c -> new BoardColumnResponse(
                                        c.getId(),
                                        c.getName(),
                                        c.getPosition(),
                                        c.getIsDone()
                                ))
                                .toList();

        return new BoardResponse(
                board.getId(),
                board.getProjectId(),
                board.getName(),
                board.getType(),
                board.getCreatedAt(),
                board.getUpdatedAt(),
                columns
        );
    }
}