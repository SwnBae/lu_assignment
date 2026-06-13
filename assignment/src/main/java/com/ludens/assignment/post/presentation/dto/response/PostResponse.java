package com.ludens.assignment.post.presentation.dto.response;

import com.ludens.assignment.post.application.dto.PostDto;

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
    public static PostResponse of(PostDto dto, long heartCount, boolean hearted) {
        String username = dto.authorDeleted() ? "탈퇴한 사용자" : dto.authorUsername();
        return new PostResponse(
                dto.id(),
                dto.title(),
                dto.description(),
                dto.createdAt(),
                dto.updatedAt(),
                username,
                heartCount,
                hearted
        );
    }
}
