package com.jira.board_service.service;

import com.jira.board_service.dto.request.CreateBoardRequest;
import com.jira.board_service.dto.request.UpdateBoardRequest;
import com.jira.board_service.dto.response.BoardResponse;
import com.jira.board_service.entity.Board;

import java.util.List;
import java.util.UUID;

public interface BoardService {
    BoardResponse create(CreateBoardRequest request, UUID userId);

    BoardResponse getByProject(UUID projectId);

    BoardResponse update(UUID boardId, UpdateBoardRequest request);

    void delete(UUID boardId);
}
