package com.jira.organization_service.repository;

import com.jira.organization_service.constance.InviteStatus;
import com.jira.organization_service.entity.OrganizationInvite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrganizationInviteRepository
        extends JpaRepository<OrganizationInvite, UUID> {

    Optional<OrganizationInvite> findByToken(String token);
    boolean existsByOrganizationIdAndEmailAndStatus(
            UUID organizationId,
            String email,
            InviteStatus status
    );
}
