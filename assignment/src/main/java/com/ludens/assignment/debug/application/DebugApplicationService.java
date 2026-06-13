package com.ludens.assignment.debug.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ludens.assignment.debug.application.dto.DebugPostDto;
import com.ludens.assignment.debug.application.dto.DebugUserDto;
import com.ludens.assignment.debug.presentation.dto.DebugUserListItem;
import com.ludens.assignment.global.exception.BusinessException;
import com.ludens.assignment.global.exception.ErrorCode;
import com.ludens.assignment.post.domain.Post;
import com.ludens.assignment.post.infrastructure.PostRepository;
import com.ludens.assignment.post.presentation.dto.response.PostResponse;
import com.ludens.assignment.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DebugApplicationService {

    private final DebugService debugService;
    private final ObjectMapper objectMapper;
    private final PostRepository postRepository;

    public Map<String, String> getUserRaw(String username) {
        DebugUserDto dto = debugService.getUserRaw(username);
        return Map.of("data", serialize(dto));
    }

    public Map<String, String> getPostRaw(Long postId) {
        DebugPostDto dto = debugService.getPostRaw(postId);
        return Map.of("data", serialize(dto));
    }

    public Map<String, List<DebugUserListItem>> getAllUsers() {
        List<User> users = debugService.getAllUsers();
        List<DebugUserListItem> items = users.stream()
                .map(user -> DebugUserListItem.of(user, postRepository.countByAuthorId(user.getId())))
                .toList();
        return Map.of("users", items);
    }

    public Map<String, List<PostResponse>> getAllPosts() {
        List<Post> posts = debugService.getAllPostsWithAuthor();
        List<PostResponse> items = posts.stream()
                .map(post -> PostResponse.of(post, 0L, false))
                .toList();
        return Map.of("posts", items);
    }

    private String serialize(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }
}
