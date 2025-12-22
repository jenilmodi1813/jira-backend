package com.jira.user_service.service.impl;

import com.jira.user_service.dtos.response.UserProfileResponse;
import com.jira.user_service.entity.UserProfile;
import com.jira.user_service.repository.UserProfileRepository;
import com.jira.user_service.service.UserProfileService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class UserProfileServiceImpl implements UserProfileService {
    private final UserProfileRepository userProfileRepository;

    public UserProfileServiceImpl(UserProfileRepository userProfileRepository) {
        this.userProfileRepository = userProfileRepository;
    }

    @Override
    public UserProfileResponse save(UUID authUserId, String email) {

        // If already exists → return existing profile
        return userProfileRepository.findById(authUserId)
                .map(this::toResponse)
                .orElseGet(() -> {

                    UserProfile profile = UserProfile.builder()
                            .authUserId(authUserId)
                            .email(email)
                            .active(true)
                            .createdAt(LocalDateTime.now())
                            .updatedAt(LocalDateTime.now())
                            .build();

                    UserProfile savedProfile = userProfileRepository.save(profile);
                    return toResponse(savedProfile);
                });
    }

    @Override
    public UserProfileResponse findById(UUID authUserId) {

        UserProfile profile = userProfileRepository.findById(authUserId)
                .orElseThrow(() -> new RuntimeException("User profile not found"));

        return toResponse(profile);
    }

    @Override
    public UserProfileResponse updateFullName(UUID authUserId, String fullName) {
        UserProfile profile = getProfile(authUserId);
        profile.setFullName(fullName);
        return toResponse(userProfileRepository.save(profile));
    }

    @Override
    public UserProfileResponse updateAvatar(UUID authUserId, String avatarUrl) {
        UserProfile profile = getProfile(authUserId);
        profile.setAvatarUrl(avatarUrl);
        return toResponse(userProfileRepository.save(profile));
    }

    @Override
    public UserProfileResponse updateTimeZone(UUID authUserId, String timeZone) {
        UserProfile profile = getProfile(authUserId);
        profile.setTimeZone(timeZone);
        return toResponse(userProfileRepository.save(profile));
    }

    private UserProfile getProfile(UUID authUserId) {
        return userProfileRepository.findById(authUserId)
                .orElseThrow(() -> new RuntimeException("Profile not found"));
    }

    @Override
    public UserProfileResponse findByEmail(String email) {

        UserProfile profile = userProfileRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return toResponse(profile);
    }


    private UserProfileResponse toResponse(UserProfile profile) {
        return new UserProfileResponse(
                profile.getAuthUserId(),
                profile.getFullName(),
                profile.getEmail(),
                profile.getAvatarUrl(),
                profile.getTimeZone()
        );
    }
}
