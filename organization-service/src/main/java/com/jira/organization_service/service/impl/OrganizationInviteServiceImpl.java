package com.jira.organization_service.service.impl;

import com.jira.organization_service.client.UserClient;
import com.jira.organization_service.constance.InviteStatus;
import com.jira.organization_service.constance.OrgRole;
import com.jira.organization_service.dtos.event.OrganizationInviteEvent;
import com.jira.organization_service.dtos.request.InviteUserRequest;
import com.jira.organization_service.dtos.response.UserProfileResponse;
import com.jira.organization_service.entity.OrganizationInvite;
import com.jira.organization_service.entity.OrganizationMember;
import com.jira.organization_service.event.publisher.InviteUserEventPublisher;
import com.jira.organization_service.exception.BadRequestException;
import com.jira.organization_service.exception.NotFoundException;
import com.jira.organization_service.repository.OrganizationInviteRepository;
import com.jira.organization_service.repository.OrganizationMemberRepository;
import com.jira.organization_service.service.OrganizationInviteService;
import feign.FeignException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
public class OrganizationInviteServiceImpl implements OrganizationInviteService {

    private final OrganizationInviteRepository inviteRepo;
    private final OrganizationMemberRepository memberRepo;
    private final RabbitTemplate rabbitTemplate;
    private final UserClient userClient;
    private final InviteUserEventPublisher eventPublisher;

    public OrganizationInviteServiceImpl(
            OrganizationInviteRepository inviteRepo,
            OrganizationMemberRepository memberRepo,
            RabbitTemplate rabbitTemplate,
            UserClient userClient,
            InviteUserEventPublisher inviteUserEventPublisher) {
        this.inviteRepo = inviteRepo;
        this.memberRepo = memberRepo;
        this.rabbitTemplate = rabbitTemplate;
        this.userClient = userClient;
        this.eventPublisher = inviteUserEventPublisher;
    }

    public void inviteUser(UUID orgId, InviteUserRequest req, UUID invitedBy) {

        // 1️ Check inviter is admin of organization
        OrganizationMember inviter = memberRepo
                .findByOrganizationIdAndUserId(orgId, invitedBy)
                .orElseThrow(() -> new BadRequestException("You are not a member of this organization"));

        if (inviter.getOrgRole() != OrgRole.ORG_ADMIN) {
            throw new BadRequestException("Only organization admin can invite users");
        }

        // 2️ Prevent duplicate pending invite
        if (inviteRepo.existsByOrganizationIdAndEmailAndStatus(
                orgId, req.email(), InviteStatus.PENDING)) {
            throw new BadRequestException("Invite already sent");
        }

        UUID invitedUserId = null;

        // 3️ Check if user already exists (call user-service)
        try {
            UserProfileResponse user = userClient.findByEmail(req.email());
            invitedUserId = user.authUserId();

            // 4️ If user exists → check membership
            if (memberRepo.existsByOrganizationIdAndUserId(orgId, invitedUserId)) {
                throw new BadRequestException("User already a member of organization");
            }

        } catch (FeignException.NotFound ex) {
            //  User not registered yet → ALLOWED (Jira behavior)
            throw new BadRequestException("Invited user does not exist");
        }

        // 5️ Create invite
        String token = UUID.randomUUID().toString();

        OrganizationInvite invite = new OrganizationInvite();
        invite.setOrganizationId(orgId);
        invite.setEmail(req.email());
        invite.setInvitedBy(invitedBy);
        invite.setToken(token);
        invite.setStatus(InviteStatus.PENDING);
        invite.setExpiresAt(LocalDateTime.now().plusDays(2));
        invite.setCreatedAt(LocalDateTime.now());

        inviteRepo.save(invite);

        // 6️ Publish invite event
        try{
            eventPublisher.publish(
                    new OrganizationInviteEvent(
                            orgId,
                            req.email(),
                            token
                    )
            );

            System.out.println("Invite send on this email: " + req.email());
        }
        catch (Exception e){
            e.printStackTrace();

        }
    }

    public void acceptInvite(String token, UUID userId) {

        OrganizationInvite invite = inviteRepo.findByToken(token)
                .orElseThrow(() -> new NotFoundException("Invite token is invalid"));

        if (invite.getStatus() != InviteStatus.PENDING) {
            throw new BadRequestException("Invite is no longer valid");
        }

        if (invite.getExpiresAt().isBefore(LocalDateTime.now())) {
            invite.setStatus(InviteStatus.EXPIRED);
            inviteRepo.save(invite);
            throw new BadRequestException("Invite has expired");
        }

        if (memberRepo.existsByOrganizationIdAndUserId(
                invite.getOrganizationId(), userId)) {
            throw new BadRequestException("User already belongs to organization");
        }

        OrganizationMember member = OrganizationMember.builder()
                .organizationId(invite.getOrganizationId())
                .userId(userId)
                .orgRole(OrgRole.MEMBER)
                .build();

        memberRepo.save(member);

        invite.setStatus(InviteStatus.ACCEPTED);
        inviteRepo.save(invite);
        System.out.println("INVITE ACCEPT");
    }

    @Override
    @Transactional
    public void rejectInvite(String token, UUID userId) {

        OrganizationInvite invite = inviteRepo.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invite not found"));

        //  already processed
        if (invite.getStatus() != InviteStatus.PENDING) {
            throw new RuntimeException("Invite already processed");
        }

        //  optional ownership check (SAFE)
        // Only reject if email belongs to logged-in user
//        UserProfileResponse user = userClient.findById(userId);
//        if (!invite.getEmail().equalsIgnoreCase(user.getEmail())) {
//            throw new RuntimeException("Invite does not belong to this user");
//        }

        //  REJECT
        invite.setStatus(InviteStatus.REJECTED);
        inviteRepo.save(invite);   // ⚠ THIS WAS LIKELY MISSING
        System.out.println("INVITE ACCEPT");
    }

}

