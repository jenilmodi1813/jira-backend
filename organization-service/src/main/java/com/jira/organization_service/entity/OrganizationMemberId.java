package com.jira.organization_service.entity;

import java.io.Serializable;
import java.util.UUID;

public class OrganizationMemberId implements Serializable {
    private UUID organizationId;
    private UUID userId;
}
