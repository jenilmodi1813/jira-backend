package com.jira.organization_service.security;

import com.jira.organization_service.constance.OrgRole;
import com.jira.organization_service.repository.OrganizationMemberRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class OrgSecurity {

    private final OrganizationMemberRepository repo;

    public OrgSecurity(OrganizationMemberRepository repo) {
        this.repo = repo;
    }

    public boolean isOrgAdmin(UUID orgId, Authentication auth) {
        UUID userId = UUID.fromString(auth.getName());

        return repo.existsByOrganizationIdAndUserIdAndOrgRole(
                orgId, userId, OrgRole.ORG_ADMIN
        );
    }
}

