package com.jira.issue_service.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "issue_types")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IssueType {

    @Id
    private UUID id;

    @Column(nullable = false, unique = true)
    private String name; // EPIC, STORY, TASK, BUG, SUBTASK

    @PrePersist
    public void onCreate() {
        this.id = UUID.randomUUID();
    }
}
