package com.jira.organization_service.dtos.event;

import java.util.UUID;

public record OrganizationInviteEvent(
        UUID organizationId,
        String email,
        String inviteToken
) {
}
