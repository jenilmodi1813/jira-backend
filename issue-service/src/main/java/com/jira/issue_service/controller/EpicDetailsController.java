package com.jira.issue_service.controller;

import com.jira.issue_service.dto.request.epicDetails.request.CreateEpicDetailsRequest;
import com.jira.issue_service.dto.response.epicDetails.response.EpicDetailsResponse;
import com.jira.issue_service.service.EpicDetailsService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/epics")
@PreAuthorize("hasAnyRole('USER','ADMIN')")
public class EpicDetailsController {

    private final EpicDetailsService service;

    public EpicDetailsController(EpicDetailsService service) {
        this.service = service;
    }

    @PostMapping("/create")
    public EpicDetailsResponse create(
//            @PathVariable UUID epicId,
            @Valid @RequestBody CreateEpicDetailsRequest request) {

        return service.create(request);
    }

    @GetMapping("/{epicId}")
    public EpicDetailsResponse get(@PathVariable UUID epicId) {
        return service.get(epicId);
    }

    @PutMapping("/{epicId}")
    public EpicDetailsResponse update(
            @PathVariable UUID epicId,
            @Valid @RequestBody CreateEpicDetailsRequest request) {

        return service.update(epicId, request);
    }
}
