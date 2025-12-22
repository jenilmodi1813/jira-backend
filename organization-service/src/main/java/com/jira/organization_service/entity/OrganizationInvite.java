package com.jira.organization_service.entity;

import com.jira.organization_service.constance.InviteStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "organization_invites",
        uniqueConstraints = @UniqueConstraint(
                columnNames = {"organization_id", "email"}
        ))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrganizationInvite {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private UUID organizationId;

    private String email;

    private UUID invitedBy;

    private String token;

    @Enumerated(EnumType.STRING)
    private InviteStatus status;

    private LocalDateTime expiresAt;

    private LocalDateTime createdAt;
}
