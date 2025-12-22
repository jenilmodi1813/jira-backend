package com.jira.organization_service.repository;

import com.jira.organization_service.constance.OrgRole;
import com.jira.organization_service.entity.OrganizationMember;
import com.jira.organization_service.entity.OrganizationMemberId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrganizationMemberRepository extends JpaRepository<OrganizationMember, OrganizationMemberId> {

    boolean existsByOrganizationIdAndUserIdAndOrgRole(
            UUID organizationId,
            UUID userId,
            OrgRole orgRole
    );

    boolean existsByOrganizationIdAndUserId(
            UUID organizationId,
            UUID userId
    );
    List<OrganizationMember> findByUserId(UUID userId);

    Optional<OrganizationMember> findByOrganizationIdAndUserId(UUID organizationId, UUID userId);
}
