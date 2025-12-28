package com.jira.board_service.service;

import com.jira.board_service.dto.request.column.CreateColumnRequest;
import com.jira.board_service.dto.request.column.UpdateColumnRequest;
import com.jira.board_service.dto.response.BoardColumnResponse;


import java.util.List;
import java.util.UUID;

public interface BoardColumnService {

    BoardColumnResponse create(UUID boardId, CreateColumnRequest request);

    List<BoardColumnResponse> getByBoard(UUID boardId);

    BoardColumnResponse update(UUID columnId, UpdateColumnRequest request);

    void delete(UUID columnId);

    void reorder(UUID boardId, UUID uuid, Integer integer);

    com.jira.board_service.dto.response.column.BoardColumnResponse getById(UUID columnId);
}
