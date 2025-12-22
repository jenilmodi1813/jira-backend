package com.jira.user_service.service;

import com.jira.user_service.dtos.response.UserProfileResponse;
import jakarta.annotation.Nullable;


import java.util.UUID;

public interface UserProfileService {
    UserProfileResponse save(UUID uuid, String email);
    @Nullable
    UserProfileResponse findById(UUID authUserId);
    UserProfileResponse updateFullName(UUID authUserId, String fullName);
    UserProfileResponse updateAvatar(UUID authUserId, String avatarUrl);
    UserProfileResponse updateTimeZone(UUID authUserId, String timeZone);

    UserProfileResponse findByEmail(String email);
}
