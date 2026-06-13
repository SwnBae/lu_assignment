package com.ludens.assignment.post.presentation.dto.response;

import com.ludens.assignment.heart.domain.PostLike;
import com.ludens.assignment.post.domain.Post;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;
import java.util.Set;

public record PostPageResponse(
        int page,
        int limit,
        long total,
        List<PostResponse> posts
) {
    public static PostPageResponse of(Page<Post> postPage, Map<Long, Long> heartCounts,
                                      Set<Long> heartedIds, int page) {
        List<PostResponse> posts = postPage.getContent().stream()
                .map(post -> PostResponse.of(
                        post,
                        heartCounts.getOrDefault(post.getId(), 0L),
                        heartedIds.contains(post.getId())
                ))
                .toList();
        return new PostPageResponse(page, postPage.getSize(), postPage.getTotalElements(), posts);
    }

    public static PostPageResponse ofHearted(Page<PostLike> postLikePage,
                                              Map<Long, Long> heartCounts, int page) {
        List<PostResponse> posts = postLikePage.getContent().stream()
                .map(pl -> PostResponse.of(
                        pl.getPost(),
                        heartCounts.getOrDefault(pl.getPost().getId(), 0L),
                        true
                ))
                .toList();
        return new PostPageResponse(page, postLikePage.getSize(), postLikePage.getTotalElements(), posts);
    }
}
