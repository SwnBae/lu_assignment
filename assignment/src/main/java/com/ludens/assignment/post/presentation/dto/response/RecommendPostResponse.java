package com.ludens.assignment.post.presentation.dto.response;

import com.ludens.assignment.post.domain.Post;

import java.util.List;
import java.util.Map;
import java.util.Set;

public record RecommendPostResponse(
        int limit,
        long total,
        List<PostResponse> posts
) {
    public static RecommendPostResponse of(List<Post> postList, long total,
                                           Map<Long, Long> heartCounts, Set<Long> heartedIds) {
        List<PostResponse> posts = postList.stream()
                .map(post -> PostResponse.of(
                        post,
                        heartCounts.getOrDefault(post.getId(), 0L),
                        heartedIds.contains(post.getId())
                ))
                .toList();
        return new RecommendPostResponse(postList.size(), total, posts);
    }
}
