package com.ludens.assignment.post.application;

import com.ludens.assignment.global.config.properties.ImageProperties;
import com.ludens.assignment.global.exception.BusinessException;
import com.ludens.assignment.global.exception.ErrorCode;
import com.ludens.assignment.heart.application.HeartQueryService;
import com.ludens.assignment.heart.infrastructure.HeartCountRedisRepository;
import com.ludens.assignment.heart.infrastructure.PostLikeRepository;
import com.ludens.assignment.post.domain.Post;
import com.ludens.assignment.post.presentation.dto.response.CreatePostResponse;
import com.ludens.assignment.post.presentation.dto.response.PostPageResponse;
import com.ludens.assignment.post.presentation.dto.response.PostResponse;
import com.ludens.assignment.post.presentation.dto.response.RecommendPostResponse;
import com.ludens.assignment.post.presentation.dto.response.UpdatePostResponse;
import com.ludens.assignment.user.application.UserService;
import com.ludens.assignment.user.infrastructure.PostCountRedisRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostApplicationService {

    private static final byte[] PNG_HEADER = {
            (byte) 0x89, 0x50, 0x4E, 0x47, 0x0D, 0x0A, 0x1A, 0x0A
    };

    private final PostService postService;
    private final UserService userService;
    private final HeartQueryService heartQueryService;
    private final HeartCountRedisRepository heartCountRedisRepository;
    private final PostCountRedisRepository postCountRedisRepository;
    private final PostLikeRepository postLikeRepository;
    private final ImageProperties imageProperties;

    public CreatePostResponse createPost(String userId, String title, String description,
                                         MultipartFile image) {
        validatePng(image);
        String uuid = UUID.randomUUID().toString();
        Path path = resolveImagePath(uuid);
        try {
            Files.createDirectories(path.getParent());
            image.transferTo(path);
        } catch (IOException e) {
            throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
        try {
            Long postId = postService.create(userId, title, description, uuid);
            postCountRedisRepository.increment(userId);
            return new CreatePostResponse(postId);
        } catch (Exception e) {
            silentDelete(path);
            throw e;
        }
    }

    public UpdatePostResponse updatePost(String userId, Long postId, String title,
                                          String description) {
        Long id = postService.update(userId, postId, title, description);
        return new UpdatePostResponse(id);
    }

    public void deletePost(String userId, Long postId) {
        String imagePath = postService.delete(userId, postId);
        heartCountRedisRepository.delete(postId);
        postCountRedisRepository.decrement(userId);
        if (imagePath != null) {
            silentDelete(resolveImagePath(imagePath));
        }
    }

    public PostResponse getPost(String loginUserId, Long postId) {
        Post post = postService.findByIdWithAuthor(postId);
        long heartCount = heartQueryService.getHeartCount(postId);
        boolean hearted = loginUserId != null
                && postLikeRepository.existsByUserIdAndPostId(loginUserId, postId);
        return PostResponse.of(post, heartCount, hearted);
    }

    public PostPageResponse getPostsByAuthor(String loginUserId, String username, int page,
                                              int limit) {
        var user = userService.findByUsername(username);
        Pageable pageable = PageRequest.of(page - 1, limit);
        Page<Post> postPage = postService.findByAuthor(user.getId(), pageable);
        List<Long> postIds = postPage.getContent().stream().map(Post::getId).toList();
        Map<Long, Long> heartCounts = heartQueryService.getHeartCounts(postIds);
        Set<Long> heartedIds = heartQueryService.getHeartedPostIds(loginUserId, postIds);
        return PostPageResponse.of(postPage, heartCounts, heartedIds, page);
    }

    public PostPageResponse searchPosts(String loginUserId, String q, int page, int limit) {
        Pageable pageable = PageRequest.of(page - 1, limit);
        Page<Post> postPage = postService.search(q == null ? "" : q, pageable);
        List<Long> postIds = postPage.getContent().stream().map(Post::getId).toList();
        Map<Long, Long> heartCounts = heartQueryService.getHeartCounts(postIds);
        Set<Long> heartedIds = heartQueryService.getHeartedPostIds(loginUserId, postIds);
        return PostPageResponse.of(postPage, heartCounts, heartedIds, page);
    }

    public RecommendPostResponse getRecommended(String loginUserId, int limit) {
        List<Post> posts = postService.findRecommended(limit);
        long total = postService.countAll();
        List<Long> postIds = posts.stream().map(Post::getId).toList();
        Map<Long, Long> heartCounts = heartQueryService.getHeartCounts(postIds);
        Set<Long> heartedIds = heartQueryService.getHeartedPostIds(loginUserId, postIds);
        return RecommendPostResponse.of(posts, total, heartCounts, heartedIds);
    }

    public Path getImagePath(Long postId) {
        Post post = postService.findByIdWithAuthor(postId);
        if (post.getImagePath() == null) return null;
        return resolveImagePath(post.getImagePath());
    }

    private void validatePng(MultipartFile file) {
        byte[] header = new byte[8];
        try (InputStream is = file.getInputStream()) {
            if (is.read(header) < 8) {
                throw new BusinessException(ErrorCode.INVALID_INPUT);
            }
        } catch (BusinessException e) {
            throw e;
        } catch (IOException e) {
            throw new BusinessException(ErrorCode.INVALID_INPUT);
        }
        if (!Arrays.equals(header, PNG_HEADER)) {
            throw new BusinessException(ErrorCode.INVALID_INPUT);
        }
    }

    private Path resolveImagePath(String imagePath) {
        return Paths.get(imageProperties.getStoragePath()).resolve(imagePath);
    }

    private void silentDelete(Path path) {
        try {
            Files.deleteIfExists(path);
        } catch (IOException e) {
            log.warn("Failed to delete image file: {}", path);
        }
    }
}
