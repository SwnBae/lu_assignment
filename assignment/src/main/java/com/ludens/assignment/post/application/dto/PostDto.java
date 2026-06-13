package com.ludens.assignment.post.application.dto;

import com.ludens.assignment.post.domain.Post;

import java.time.LocalDateTime;

public record PostDto(
        Long id,
        String title,
        String description,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        String imagePath,
        String authorId,
        String authorUsername,
        boolean authorDeleted
) {
    public static PostDto from(Post post) {
        return new PostDto(
                post.getId(),
                post.getTitle(),
                post.getDescription(),
                post.getCreatedAt(),
                post.getUpdatedAt(),
                post.getImagePath(),
                post.getAuthor().getId(),
                post.getAuthor().getUsername(),
                post.getAuthor().isDeleted()
        );
    }
}
