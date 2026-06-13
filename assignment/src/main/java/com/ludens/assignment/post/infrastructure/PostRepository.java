package com.ludens.assignment.post.infrastructure;

import com.ludens.assignment.post.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("SELECT p FROM Post p JOIN FETCH p.author WHERE p.id = :id")
    Optional<Post> findByIdWithAuthor(@Param("id") Long id);

    @Query(value = "SELECT p FROM Post p JOIN FETCH p.author WHERE p.author.id = :authorId",
            countQuery = "SELECT COUNT(p) FROM Post p WHERE p.author.id = :authorId")
    Page<Post> findByAuthorWithAuthor(@Param("authorId") String authorId, Pageable pageable);

    @Query(value = """
            SELECT * FROM posts
            WHERE (:q = '' OR title ILIKE '%' || :q || '%'
                   OR description ILIKE '%' || :q || '%')
            ORDER BY created_at DESC, id DESC
            """,
            countQuery = """
            SELECT COUNT(*) FROM posts
            WHERE (:q = '' OR title ILIKE '%' || :q || '%'
                   OR description ILIKE '%' || :q || '%')
            """,
            nativeQuery = true)
    Page<Post> searchPosts(@Param("q") String q, Pageable pageable);

    @Query(value = "SELECT * FROM posts ORDER BY created_at DESC LIMIT :limit", nativeQuery = true)
    List<Post> findRecommended(@Param("limit") int limit);

    long countByAuthorId(String authorId);

    @Query("SELECT p.author.id, COUNT(p) FROM Post p GROUP BY p.author.id")
    List<Object[]> countAllGroupByAuthorId();

    @Query("SELECT p FROM Post p JOIN FETCH p.author")
    List<Post> findAllWithAuthor();
}
