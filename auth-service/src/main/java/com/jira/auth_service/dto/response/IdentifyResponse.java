package com.jira.auth_service.dto.response;

public record IdentifyResponse(
        boolean exists,
        boolean verified
) {
}
