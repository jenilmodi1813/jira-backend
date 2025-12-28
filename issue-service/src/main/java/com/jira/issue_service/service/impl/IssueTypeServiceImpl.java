package com.jira.issue_service.service.impl;

import com.jira.issue_service.dto.request.issueType.request.CreateIssueTypeRequest;
import com.jira.issue_service.dto.response.issueType.response.IssueTypeResponse;
import com.jira.issue_service.entity.IssueType;
import com.jira.issue_service.exception.BadRequestException;
import com.jira.issue_service.repository.IssueTypeRepository;
import com.jira.issue_service.service.IssueTypeService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class IssueTypeServiceImpl implements IssueTypeService {

    private final IssueTypeRepository repo;

    public IssueTypeServiceImpl(IssueTypeRepository repo) {
        this.repo = repo;
    }

    @Override
    public IssueTypeResponse create(CreateIssueTypeRequest req) {

        if (repo.existsByName(req.name())) {
            throw new BadRequestException("Issue type already exists");
        }
        UUID id = UUID.randomUUID();
        IssueType type = repo.save(
                IssueType.builder()
                        .id(id)
                        .name(req.name().toUpperCase())
                        .build()
        );

        return new IssueTypeResponse(type.getId(), type.getName());
    }

    @Override
    public List<IssueTypeResponse> getAll() {
        return repo.findAll()
                .stream()
                .map(t -> new IssueTypeResponse(t.getId(), t.getName()))
                .toList();
    }

    @Override
    public void delete(UUID id) {
        repo.deleteById(id);
    }
}
