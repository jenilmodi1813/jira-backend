package com.jira.issue_service.service.impl;

import com.jira.issue_service.dto.request.epicDetails.request.CreateEpicDetailsRequest;
import com.jira.issue_service.dto.response.epicDetails.response.EpicDetailsResponse;
import com.jira.issue_service.entity.EpicDetails;
import com.jira.issue_service.entity.Issue;
import com.jira.issue_service.exception.BadRequestException;
import com.jira.issue_service.exception.ResourceNotFoundException;
import com.jira.issue_service.repository.EpicDetailsRepository;
import com.jira.issue_service.repository.IssueRepository;
import com.jira.issue_service.service.EpicDetailsService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Transactional
public class EpicDetailsServiceImpl implements EpicDetailsService {

    private final EpicDetailsRepository repo;
    private final IssueRepository issueRepo;

    public EpicDetailsServiceImpl(
            EpicDetailsRepository repo,
            IssueRepository issueRepo) {

        this.repo = repo;
        this.issueRepo = issueRepo;
    }

    @Override
    public EpicDetailsResponse create(CreateEpicDetailsRequest req) {

//        Issue epic = issueRepo.findById(epicId)
//                .orElseThrow(() -> new ResourceNotFoundException("Epic not found"));

//        if (!epic.getIssueTypeId().toString().equalsIgnoreCase("EPIC")) {
//            throw new BadRequestException("Issue is not an EPIC");
//        }
        UUID id = UUID.randomUUID();
        EpicDetails details = repo.save(
                EpicDetails.builder()
                        .epicId(id)
                        .epicName(req.epicName())
                        .epicColor(req.epicColor())
                        .build()
        );

        return map(details);
    }

    @Override
    public EpicDetailsResponse get(UUID epicId) {
        return map(
                repo.findById(epicId)
                        .orElseThrow(() -> new ResourceNotFoundException("Epic details not found"))
        );
    }

    @Override
    public EpicDetailsResponse update(UUID epicId, CreateEpicDetailsRequest req) {

        EpicDetails details = repo.findById(epicId)
                .orElseThrow(() -> new ResourceNotFoundException("Epic details not found"));

        details.setEpicName(req.epicName());
        details.setEpicColor(req.epicColor());

        return map(details);
    }

    private EpicDetailsResponse map(EpicDetails d) {
        return new EpicDetailsResponse(
                d.getEpicId(),
                d.getEpicName(),
                d.getEpicColor()
        );
    }
}

