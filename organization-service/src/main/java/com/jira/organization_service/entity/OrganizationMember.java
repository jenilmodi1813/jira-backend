package com.jira.organization_service.entity;

import com.jira.organization_service.constance.OrgRole;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "organization_members")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@IdClass(OrganizationMemberId.class)
public class OrganizationMember {

    @Id
    private UUID organizationId;

    @Id
    private UUID userId;

    private String jobTitle;
    private String department;

    @Enumerated(EnumType.STRING)
    private OrgRole orgRole; // ORG_ADMIN, MEMBER

    private LocalDateTime joinedAt;

    @PrePersist
    void onJoin() {
        joinedAt = LocalDateTime.now();
    }
}
