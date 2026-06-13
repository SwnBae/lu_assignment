package com.ludens.assignment.heart.presentation.dto.response;

import com.ludens.assignment.heart.domain.PostLike;
import org.springframework.data.domain.Page;

import java.util.List;

public record HeartUsersPageResponse(
        List<String> users,
        int page,
        int limit,
        long total
) {
    public static HeartUsersPageResponse of(Page<PostLike> postLikePage, int page) {
        List<String> users = postLikePage.getContent().stream()
                .map(pl -> pl.getUser().getUsername())
                .toList();
        return new HeartUsersPageResponse(
                users, page, postLikePage.getSize(), postLikePage.getTotalElements());
    }
}
