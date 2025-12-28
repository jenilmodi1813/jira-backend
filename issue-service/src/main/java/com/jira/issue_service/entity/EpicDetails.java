package com.jira.issue_service.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "epic_details")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EpicDetails {

    @Id
    @Column(name = "epic_id")
    private UUID epicId; // FK → issues.id

    @Column(name = "epic_name", nullable = false)
    private String epicName;

    @Column(name = "epic_color")
    private String epicColor;
}
