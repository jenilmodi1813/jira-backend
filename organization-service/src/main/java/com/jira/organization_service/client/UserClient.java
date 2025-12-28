package com.jira.organization_service.client;

import com.jira.organization_service.dtos.response.UserProfileResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@FeignClient(name = "user-service")
public interface UserClient {

    @GetMapping("/api/users/profile/find-by-email")
    UserProfileResponse findByEmail(@RequestParam String email);

    @GetMapping("/api/users/profile/{userId}")
    UserProfileResponse getProfile(@PathVariable UUID userId);
}
