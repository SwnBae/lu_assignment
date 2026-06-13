package com.ludens.assignment.post.presentation.dto.response;

import com.ludens.assignment.post.application.dto.PostDto;

import java.util.List;
import java.util.Map;
import java.util.Set;

public record RecommendPostResponse(
        int limit,
        long total,
        List<PostResponse> posts
) {
    public static RecommendPostResponse of(List<PostDto> dtos, long total,
                                           Map<Long, Long> heartCounts, Set<Long> heartedIds) {
        List<PostResponse> posts = dtos.stream()
                .map(dto -> PostResponse.of(
                        dto,
                        heartCounts.getOrDefault(dto.id(), 0L),
                        heartedIds.contains(dto.id())
                ))
                .toList();
        return new RecommendPostResponse(dtos.size(), total, posts);
    }
}
