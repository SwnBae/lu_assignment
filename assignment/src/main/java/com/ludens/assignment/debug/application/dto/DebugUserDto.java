package com.ludens.assignment.debug.application.dto;

import com.ludens.assignment.user.domain.User;

import java.time.LocalDateTime;

public record DebugUserDto(
        String id,
        String username,
        String password,
        String role,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        LocalDateTime deletedAt
) {
    public static DebugUserDto from(User user) {
        return new DebugUserDto(
                user.getId(),
                user.getUsername(),
                user.getPassword(),
                user.getRole().name(),
                user.getCreatedAt(),
                user.getUpdatedAt(),
                user.getDeletedAt()
        );
    }
}
