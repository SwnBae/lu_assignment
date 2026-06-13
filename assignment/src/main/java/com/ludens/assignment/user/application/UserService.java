package com.ludens.assignment.user.application;

import com.ludens.assignment.post.infrastructure.PostRepository;
import com.ludens.assignment.user.domain.User;
import com.ludens.assignment.user.exception.UserException;
import com.ludens.assignment.user.infrastructure.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;

    @Transactional(readOnly = true)
    public User findByUsername(String username) {
        return userRepository.findByUsernameAndDeletedAtIsNull(username)
                .orElseThrow(UserException::notFound);
    }

    @Transactional(readOnly = true)
    public long countPosts(String userId) {
        return postRepository.countByAuthorId(userId);
    }
}
