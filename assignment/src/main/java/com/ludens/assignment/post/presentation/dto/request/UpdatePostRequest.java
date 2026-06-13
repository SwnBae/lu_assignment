package com.ludens.assignment.post.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;

public record UpdatePostRequest(
        @NotBlank String title,
        String description
) {
}
