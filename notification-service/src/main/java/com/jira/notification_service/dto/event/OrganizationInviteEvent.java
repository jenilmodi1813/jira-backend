package com.jira.notification_service.dto.event;

import java.util.UUID;

public record OrganizationInviteEvent(
        UUID organizationId,
        String email,
        String inviteToken
) {
}
