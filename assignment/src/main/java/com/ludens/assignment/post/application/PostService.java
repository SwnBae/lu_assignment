package com.ludens.assignment.post.application;

import com.ludens.assignment.post.domain.Post;
import com.ludens.assignment.post.exception.PostException;
import com.ludens.assignment.post.infrastructure.PostRepository;
import com.ludens.assignment.user.infrastructure.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional
    public Long create(String userId, String title, String description, String imagePath) {
        var user = userRepository.getReferenceById(userId);
        var post = Post.create(title, description, user, imagePath);
        return postRepository.save(post).getId();
    }

    @Transactional(readOnly = true)
    public Post findByIdWithAuthor(Long postId) {
        return postRepository.findByIdWithAuthor(postId)
                .orElseThrow(PostException::notFound);
    }

    @Transactional
    public Long update(String userId, Long postId, String title, String description) {
        var post = postRepository.findById(postId).orElseThrow(PostException::notFound);
        if (!post.getAuthor().getId().equals(userId)) {
            throw PostException.forbidden();
        }
        post.update(title, description);
        return post.getId();
    }

    @Transactional
    public String delete(String userId, Long postId) {
        var post = postRepository.findById(postId).orElseThrow(PostException::notFound);
        if (!post.getAuthor().getId().equals(userId)) {
            throw PostException.forbidden();
        }
        String imagePath = post.getImagePath();
        postRepository.delete(post);
        return imagePath;
    }

    @Transactional(readOnly = true)
    public Page<Post> findByAuthor(String authorId, Pageable pageable) {
        return postRepository.findByAuthorWithAuthor(authorId, pageable);
    }

    @Transactional(readOnly = true)
    public Page<Post> search(String q, Pageable pageable) {
        return postRepository.searchPosts(q, pageable);
    }

    @Transactional(readOnly = true)
    public List<Post> findRecommended(int limit) {
        return postRepository.findRecommended(limit);
    }

    @Transactional(readOnly = true)
    public long countByAuthor(String authorId) {
        return postRepository.countByAuthorId(authorId);
    }

    @Transactional(readOnly = true)
    public long countAll() {
        return postRepository.count();
    }
}
