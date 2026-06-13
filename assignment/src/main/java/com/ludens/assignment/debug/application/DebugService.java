package com.ludens.assignment.debug.application;

import com.ludens.assignment.debug.application.dto.DebugPostDto;
import com.ludens.assignment.debug.application.dto.DebugUserDto;
import com.ludens.assignment.global.exception.BusinessException;
import com.ludens.assignment.global.exception.ErrorCode;
import com.ludens.assignment.post.application.dto.PostDto;
import com.ludens.assignment.post.domain.Post;
import com.ludens.assignment.post.infrastructure.PostRepository;
import com.ludens.assignment.user.domain.User;
import com.ludens.assignment.user.infrastructure.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DebugService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;

    @Transactional(readOnly = true)
    public DebugUserDto getUserRaw(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
        return DebugUserDto.from(user);
    }

    @Transactional(readOnly = true)
    public DebugPostDto getPostRaw(Long postId) {
        Post post = postRepository.findByIdWithAuthor(postId)
                .orElseThrow(() -> new BusinessException(ErrorCode.POST_NOT_FOUND));
        return DebugPostDto.from(post);
    }

    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return userRepository.findByDeletedAtIsNull();
    }

    @Transactional(readOnly = true)
    public List<PostDto> getAllPostDtos() {
        return postRepository.findAllWithAuthor().stream()
                .map(PostDto::from)
                .toList();
    }
}
