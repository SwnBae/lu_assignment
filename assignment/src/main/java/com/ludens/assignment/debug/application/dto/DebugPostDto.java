package com.ludens.assignment.debug.application.dto;

import com.ludens.assignment.post.domain.Post;

import java.time.LocalDateTime;

public record DebugPostDto(
        Long id,
        String title,
        String description,
        String authorId,
        String imagePath,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static DebugPostDto from(Post post) {
        return new DebugPostDto(
                post.getId(),
                post.getTitle(),
                post.getDescription(),
                post.getAuthor().getId(),
                post.getImagePath(),
                post.getCreatedAt(),
                post.getUpdatedAt()
        );
    }
}
