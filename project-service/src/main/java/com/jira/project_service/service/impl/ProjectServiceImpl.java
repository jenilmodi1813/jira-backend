package com.jira.project_service.service.impl;

import com.jira.project_service.dto.request.CreateProjectRequest;
import com.jira.project_service.dto.request.UpdateProjectRequest;
import com.jira.project_service.dto.response.ProjectResponse;
import com.jira.project_service.entity.Project;
import com.jira.project_service.exception.BadRequestException;
import com.jira.project_service.exception.ResourceNotFoundException;
import com.jira.project_service.repository.ProjectRepository;
import com.jira.project_service.service.ProjectService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepo;

    public ProjectServiceImpl(ProjectRepository projectRepo) {
        this.projectRepo = projectRepo;
    }

    @Override
    public ProjectResponse create(CreateProjectRequest req, UUID userId) {

        if (projectRepo.existsByProjectKey(req.projectKey())) {
            throw new BadRequestException("Project key already exists");
        }

        Project project = Project.builder()
                .organizationId(req.organizationId())
                .name(req.name())
                .projectKey(req.projectKey().toUpperCase())
                .projectType(req.projectType())
                .leadId(req.leadId())
                .build();

        projectRepo.save(project);
        return map(project);
    }

    @Override
    public ProjectResponse getById(UUID projectId) {
        return map(find(projectId));
    }

    @Override
    public List<ProjectResponse> getByOrganization(UUID organizationId) {
        return projectRepo.findByOrganizationId(organizationId)
                .stream()
                .map(this::map)
                .toList();
    }

    @Override
    public ProjectResponse update(UUID projectId, UpdateProjectRequest req) {
        Project project = find(projectId);

        if (req.name() != null) project.setName(req.name());
        if (req.projectType() != null) project.setProjectType(req.projectType());
        if (req.leadId() != null) project.setLeadId(req.leadId());

        return map(projectRepo.save(project));
    }

    @Override
    public void delete(UUID projectId) {
        if (!projectRepo.existsById(projectId)) {
            throw new ResourceNotFoundException("Project not found");
        }
        projectRepo.deleteById(projectId);
    }

    private Project find(UUID id) {
        return projectRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));
    }

    @Override
    public List<ProjectResponse> getByLead(UUID leadId) {

        return projectRepo.findByLeadId(leadId)
                .stream()
                .map(this::map)
                .toList();
    }


    private ProjectResponse map(Project p) {
        return new ProjectResponse(
                p.getId(),
                p.getOrganizationId(),
                p.getName(),
                p.getProjectKey(),
                p.getProjectType(),
                p.getLeadId(),
                p.getCreatedAt()
        );
    }
}
