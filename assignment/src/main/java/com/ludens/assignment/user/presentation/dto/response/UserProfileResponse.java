package com.ludens.assignment.user.presentation.dto.response;

import com.ludens.assignment.user.domain.User;

import java.time.LocalDateTime;

public record UserProfileResponse(
        String username,
        long postCount,
        LocalDateTime createdAt
) {
    public static UserProfileResponse of(User user, long postCount) {
        return new UserProfileResponse(user.getUsername(), postCount, user.getCreatedAt());
    }
}
