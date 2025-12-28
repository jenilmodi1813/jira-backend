package com.jira.issue_service.repository;

import com.jira.issue_service.entity.EpicDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface EpicDetailsRepository extends JpaRepository<EpicDetails, UUID> {
}

