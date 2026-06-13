package com.ludens.assignment.heart.application;

import com.ludens.assignment.heart.domain.PostLike;
import com.ludens.assignment.heart.exception.HeartException;
import com.ludens.assignment.heart.infrastructure.PostLikeRepository;
import com.ludens.assignment.post.exception.PostException;
import com.ludens.assignment.post.infrastructure.PostRepository;
import com.ludens.assignment.user.infrastructure.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostLikeService {

    private final PostLikeRepository postLikeRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional
    public void addLike(String userId, Long postId) {
        if (!postRepository.existsById(postId)) {
            throw PostException.notFound();
        }
        try {
            postLikeRepository.save(PostLike.create(
                    userRepository.getReferenceById(userId),
                    postRepository.getReferenceById(postId)
            ));
        } catch (DataIntegrityViolationException e) {
            throw HeartException.alreadyHearted();
        }
    }

    @Transactional
    public void removeLike(String userId, Long postId) {
        if (postLikeRepository.deleteByUserIdAndPostId(userId, postId) == 0) {
            if (!postRepository.existsById(postId)) {
                throw PostException.notFound();
            }
            throw HeartException.notHearted();
        }
    }

    @Transactional(readOnly = true)
    public Page<PostLike> getPostHearts(String loginUserId, Long postId, Pageable pageable) {
        var post = postRepository.findById(postId).orElseThrow(PostException::notFound);
        if (!post.getAuthor().getId().equals(loginUserId)) {
            throw PostException.forbidden();
        }
        return postLikeRepository.findByPostIdWithUser(postId, pageable);
    }

    @Transactional(readOnly = true)
    public Page<PostLike> getUserHearts(String userId, Pageable pageable) {
        return postLikeRepository.findByUserIdWithPost(userId, pageable);
    }
}
