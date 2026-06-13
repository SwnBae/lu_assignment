package com.ludens.assignment.post.presentation.dto.response;

import com.ludens.assignment.post.application.dto.PostDto;
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
    public static PostPageResponse of(Page<PostDto> dtoPage, Map<Long, Long> heartCounts,
                                      Set<Long> heartedIds, int page) {
        List<PostResponse> posts = dtoPage.getContent().stream()
                .map(dto -> PostResponse.of(
                        dto,
                        heartCounts.getOrDefault(dto.id(), 0L),
                        heartedIds.contains(dto.id())
                ))
                .toList();
        return new PostPageResponse(page, dtoPage.getSize(), dtoPage.getTotalElements(), posts);
    }

    public static PostPageResponse ofHearted(Page<PostDto> dtoPage,
                                              Map<Long, Long> heartCounts, int page) {
        List<PostResponse> posts = dtoPage.getContent().stream()
                .map(dto -> PostResponse.of(
                        dto,
                        heartCounts.getOrDefault(dto.id(), 0L),
                        true
                ))
                .toList();
        return new PostPageResponse(page, dtoPage.getSize(), dtoPage.getTotalElements(), posts);
    }
}
