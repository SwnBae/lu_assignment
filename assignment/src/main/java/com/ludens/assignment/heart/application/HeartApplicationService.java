package com.ludens.assignment.heart.application;

import com.ludens.assignment.global.exception.BusinessException;
import com.ludens.assignment.global.exception.ErrorCode;
import com.ludens.assignment.heart.domain.PostLike;
import com.ludens.assignment.heart.infrastructure.HeartCountRedisRepository;
import com.ludens.assignment.heart.presentation.dto.response.HeartUsersPageResponse;
import com.ludens.assignment.post.application.dto.PostDto;
import com.ludens.assignment.post.presentation.dto.response.PostPageResponse;
import com.ludens.assignment.user.application.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class HeartApplicationService {

    private final PostLikeService postLikeService;
    private final HeartCountRedisRepository heartCountRedisRepository;
    private final HeartQueryService heartQueryService;
    private final UserService userService;

    public void heart(String userId, Long postId) {
        postLikeService.addLike(userId, postId);
        heartCountRedisRepository.increment(postId);
    }

    public void unheart(String userId, Long postId) {
        postLikeService.removeLike(userId, postId);
        heartCountRedisRepository.decrement(postId);
    }

    public HeartUsersPageResponse getPostHearts(String loginUserId, Long postId, int page, int limit) {
        Page<PostLike> postLikePage = postLikeService.getPostHearts(
                loginUserId, postId, PageRequest.of(page - 1, limit));
        return HeartUsersPageResponse.of(postLikePage, page);
    }

    public PostPageResponse getUserHearts(String loginUserId, String username, int page, int limit) {
        var user = userService.findByUsername(username);
        if (!user.getId().equals(loginUserId)) {
            throw new BusinessException(ErrorCode.FORBIDDEN);
        }
        Page<PostDto> dtoPage = postLikeService.getUserHearts(
                user.getId(), PageRequest.of(page - 1, limit));
        List<Long> postIds = dtoPage.getContent().stream().map(PostDto::id).toList();
        Map<Long, Long> heartCounts = heartQueryService.getHeartCounts(postIds);
        return PostPageResponse.ofHearted(dtoPage, heartCounts, page);
    }
}
