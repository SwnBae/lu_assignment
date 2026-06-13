package com.ludens.assignment.debug.presentation.dto;

import com.ludens.assignment.user.domain.User;

import java.time.LocalDateTime;

public record DebugUserListItem(
        String username,
        long postCount,
        LocalDateTime createdAt
) {
    public static DebugUserListItem of(User user, long postCount) {
        return new DebugUserListItem(user.getUsername(), postCount, user.getCreatedAt());
    }
}
