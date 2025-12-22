package com.jira.organization_service.service;

import com.jira.organization_service.dtos.request.CreateOrganizationRequest;
import com.jira.organization_service.dtos.response.OrganizationResponse;

import java.util.List;
import java.util.UUID;

public interface OrganizationService {
    OrganizationResponse createOrganization(CreateOrganizationRequest req, UUID userId);

    List<OrganizationResponse> myOrganizations(UUID userId);
}
