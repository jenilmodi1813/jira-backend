package com.jira.project_service.repository;

import com.jira.project_service.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProjectRepository extends JpaRepository<Project, UUID> {
    boolean existsByProjectKey(String projectKey);

    List<Project> findByOrganizationId(UUID organizationId);

    Optional<Project> findByProjectKey(String projectKey);

    List<Project> findByLeadId(UUID leadId);
}
