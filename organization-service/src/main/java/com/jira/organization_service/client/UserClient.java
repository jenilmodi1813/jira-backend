package com.jira.organization_service.client;

import com.jira.organization_service.dtos.response.UserProfileResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "user-service")
public interface UserClient {

    @GetMapping("/api/users/profile/find-by-email")
    UserProfileResponse findByEmail(@RequestParam String email);
}
