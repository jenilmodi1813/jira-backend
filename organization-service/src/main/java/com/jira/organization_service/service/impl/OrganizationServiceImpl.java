package com.jira.organization_service.service.impl;

import com.jira.organization_service.client.UserClient;
import com.jira.organization_service.constance.OrgRole;
import com.jira.organization_service.dtos.request.CreateOrganizationRequest;
import com.jira.organization_service.dtos.response.OrganizationMemberResponse;
import com.jira.organization_service.dtos.response.OrganizationResponse;
import com.jira.organization_service.dtos.response.UserProfileResponse;
import com.jira.organization_service.entity.Organization;
import com.jira.organization_service.entity.OrganizationMember;
import com.jira.organization_service.exception.ForbiddenException;
import com.jira.organization_service.repository.OrganizationMemberRepository;
import com.jira.organization_service.repository.OrganizationRepository;
import com.jira.organization_service.service.OrganizationService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class OrganizationServiceImpl implements OrganizationService  {

    private final OrganizationRepository orgRepo;
    private final OrganizationMemberRepository memberRepo;
    private final UserClient userClient;

    public OrganizationServiceImpl(
            OrganizationRepository orgRepo,
            OrganizationMemberRepository memberRepo,
            UserClient userClient) {
        this.orgRepo = orgRepo;
        this.memberRepo = memberRepo;
        this.userClient = userClient;
    }

    public OrganizationResponse createOrganization(
            CreateOrganizationRequest request,
            UUID userId) {

        Organization org = Organization.builder()
                .name(request.name())
                .ownerId(userId)
                .build();

        orgRepo.save(org);

        OrganizationMember owner = OrganizationMember.builder()
                .organizationId(org.getId())
                .userId(userId)
                .orgRole(OrgRole.ORG_ADMIN)
                .build();

        memberRepo.save(owner);

        return new OrganizationResponse(
                org.getId(),
                org.getName(),
                org.getOwnerId()
        );
    }

    public List<OrganizationResponse> myOrganizations(UUID userId) {
        return memberRepo.findByUserId(userId)
                .stream()
                .map(m -> orgRepo.findById(m.getOrganizationId()).orElseThrow())
                .map(o -> new OrganizationResponse(o.getId(), o.getName(), o.getOwnerId()))
                .toList();
    }

    @Override
    @Transactional

    public List<OrganizationMemberResponse> getMembers(UUID orgId, UUID requesterId) {

        //  Security: requester must be org member
        if (!memberRepo.existsByOrganizationIdAndUserId(orgId, requesterId)) {
            throw new ForbiddenException("Not a member of this organization");
        }

        return memberRepo.findByOrganizationId(orgId)
                .stream()
                .map(m -> {
                    UserProfileResponse user =
                            userClient.getProfile(m.getUserId());

                    return new OrganizationMemberResponse(
                            m.getUserId(),
                            user.email(),
                            user.fullName(),
                            user.avatarUrl(),
                            m.getOrgRole().name(),
                            m.getJobTitle(),
                            m.getDepartment(),
                            m.getJoinedAt()
                    );
                })
                .toList();
    }

}
