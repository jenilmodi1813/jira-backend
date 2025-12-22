package com.jira.organization_service.service;

import com.jira.organization_service.dtos.request.InviteUserRequest;

import java.util.UUID;

public interface OrganizationInviteService {
    void inviteUser(UUID orgId, InviteUserRequest req, UUID userId);

    void acceptInvite(String token, UUID userId);

    void rejectInvite(String token, UUID userId);
}
