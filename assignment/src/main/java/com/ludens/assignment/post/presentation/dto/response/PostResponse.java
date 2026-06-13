package com.ludens.assignment.post.presentation.dto.response;

import com.ludens.assignment.post.domain.Post;

import java.time.LocalDateTime;

public record PostResponse(
        Long id,
        String title,
        String description,
        LocalDateTime createdAt,
        LocalDateTime editedAt,
        String username,
        long heartCount,
        boolean hearted
) {
    public static PostResponse of(Post post, long heartCount, boolean hearted) {
        String username = post.getAuthor().isDeleted()
                ? "탈퇴한 사용자"
                : post.getAuthor().getUsername();
        return new PostResponse(
                post.getId(),
                post.getTitle(),
                post.getDescription(),
                post.getCreatedAt(),
                post.getUpdatedAt(),
                username,
                heartCount,
                hearted
        );
    }
}
