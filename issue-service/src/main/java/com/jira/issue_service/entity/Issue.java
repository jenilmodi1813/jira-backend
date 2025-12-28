package com.jira.issue_service.entity;

import com.jira.issue_service.constance.IssuePriority;
import com.jira.issue_service.constance.IssueStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "issues")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Issue {

    @Id
    private UUID id;

    //  Cross-service references (NO JPA relation)
    @Column(name = "project_id", nullable = false)
    private UUID projectId;

    @Column(name = "board_column_id")
    private UUID boardColumnId;

    @Column(name = "issue_type_id", nullable = false)
    private UUID issueTypeId;

    //  Hierarchy
    @ManyToOne
    @JoinColumn(name = "parent_issue_id")
    private Issue parentIssue; // SUBTASK

    @Column(name = "epic_id")
    private UUID epicId; // STORY/TASK → EPIC

    //  Core fields
    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "text")
    private String description;

    private String status;   // TODO, IN_PROGRESS, DONE
    private String priority; // LOW, MEDIUM, HIGH

    // 👤 Users
    @Column(name = "assignee_id")
    private UUID assigneeId;

    @Column(name = "reporter_id")
    private UUID reporterId;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    public void onCreate() {
        this.id = UUID.randomUUID();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
