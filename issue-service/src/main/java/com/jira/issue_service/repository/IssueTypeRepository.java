package com.jira.issue_service.repository;

import com.jira.issue_service.entity.Issue;
import com.jira.issue_service.entity.IssueType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;

import java.util.Optional;
import java.util.UUID;
@Repository
public interface IssueTypeRepository extends JpaRepository<IssueType, UUID> {
    boolean existsByName(String name);

    Optional<IssueType> findByName(String subtask);
}
