package com.jira.issue_service.service.impl;

import com.jira.issue_service.client.BoardClient;
import com.jira.issue_service.dto.request.CreateIssueRequest;
import com.jira.issue_service.dto.request.CreateSubTaskRequest;
import com.jira.issue_service.dto.request.UpdateIssueRequest;
import com.jira.issue_service.dto.response.IssueResponse;

import com.jira.issue_service.dto.response.board.BoardColumnResponse;
import com.jira.issue_service.entity.Issue;
import com.jira.issue_service.entity.IssueType;
import com.jira.issue_service.exception.BadRequestException;
import com.jira.issue_service.exception.ResourceNotFoundException;
import com.jira.issue_service.repository.IssueRepository;
import com.jira.issue_service.repository.IssueTypeRepository;
import com.jira.issue_service.service.IssueService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class IssueServiceImpl implements IssueService {

    private final IssueRepository repo;
    private final IssueTypeRepository issueTypeRepository;
    private final BoardClient boardClient;

    public IssueServiceImpl(IssueRepository repo,IssueTypeRepository issueTypeRepository,BoardClient boardClient) {
        this.repo = repo;
        this.issueTypeRepository = issueTypeRepository;
        this.boardClient = boardClient;
    }

    @Override
    public IssueResponse create(CreateIssueRequest req, UUID reporterId) {
        UUID id = UUID.randomUUID();
        Issue issue = Issue.builder()
                .id(id)
                .projectId(req.projectId())
                .issueTypeId(req.issueTypeId())
                .boardColumnId(req.boardColumnId())
//                .parentIssueId(req.parentIssueId())
                .epicId(req.epicId())
                .title(req.title())
                .description(req.description())
                .priority(req.priority())
                .status("TODO")
                .assigneeId(req.assigneeId())
                .reporterId(reporterId)
                .build();

        return map(repo.save(issue));
    }

    @Override
    public IssueResponse getById(UUID issueId) {
        return map(
                repo.findById(issueId)
                        .orElseThrow(() -> new ResourceNotFoundException("Issue not found"))
        );
    }

    @Override
    public List<IssueResponse> getByProject(UUID projectId) {
        return repo.findByProjectId(projectId)
                .stream()
                .map(this::map)
                .toList();
    }

    @Override
    public IssueResponse update(UUID issueId, UpdateIssueRequest req) {

        Issue issue = repo.findById(issueId)
                .orElseThrow(() -> new ResourceNotFoundException("Issue not found"));

        issue.setTitle(req.title());
        issue.setDescription(req.description());
        issue.setPriority(req.priority());
        issue.setStatus(req.status());
        issue.setAssigneeId(req.assigneeId());
        issue.setBoardColumnId(req.boardColumnId());

        return map(repo.save(issue));
    }

    @Override
    public void delete(UUID issueId) {
        if (!repo.existsById(issueId)) {
            throw new ResourceNotFoundException("Issue not found");
        }
        repo.deleteById(issueId);
    }

    @Override
    @Transactional
    public IssueResponse createSubTask(UUID parentIssueId, CreateSubTaskRequest req) {

        Issue parent = repo.findById(parentIssueId)
                .orElseThrow(() -> new ResourceNotFoundException("Parent issue not found"));

        //  Parent cannot be SUBTASK
        if (parent.getParentIssue() != null) {
            throw new BadRequestException("Cannot create subtask under another subtask");
        }

        IssueType subTaskType = issueTypeRepository.findByName("SUBTASK")
                .orElseThrow(() -> new ResourceNotFoundException("SUBTASK type not found"));

        Issue subTask = Issue.builder()
                .id(UUID.randomUUID())
                .projectId(parent.getProjectId())
                .parentIssue(parent)
                .epicId(parent.getEpicId())
                .boardColumnId(parent.getBoardColumnId())
                .issueTypeId(subTaskType.getId())
                .title(req.title())
                .description(req.description())
                .priority(req.priority())
                .status("TODO")
                .assigneeId(req.assigneeId())
                .reporterId(parent.getReporterId())
                .build();

        repo.save(subTask);

        return map(subTask);
    }

    @Override
    @Transactional
    public IssueResponse move(UUID issueId, UUID columnId) {

        Issue issue = repo.findById(issueId)
                .orElseThrow(() -> new ResourceNotFoundException("Issue not found"));

        BoardColumnResponse column = boardClient.getById(columnId);

        //  SAME BOARD CHECK
//        if (!fromColumn.boardId().equals(toColumn.boardId())) {
//            throw new BadRequest("Cannot move issue across boards");
//        }

        //  RULE VALIDATION
//        validateTransition(fromColumn, toColumn);

        issue.setBoardColumnId(columnId);
        issue.setUpdatedAt(LocalDateTime.now());

        return map(repo.save(issue));
    }

    private IssueResponse map(Issue i) {
        return new IssueResponse(
                i.getId(),
                i.getProjectId(),
                i.getIssueTypeId(),
                i.getBoardColumnId(),
                i.getParentIssue(),
                i.getEpicId(),
                i.getTitle(),
                i.getDescription(),
                i.getStatus(),
                i.getPriority(),
                i.getAssigneeId(),
                i.getReporterId(),
                i.getCreatedAt(),
                i.getUpdatedAt()
        );
    }
}

