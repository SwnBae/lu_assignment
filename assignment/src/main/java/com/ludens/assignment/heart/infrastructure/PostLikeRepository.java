package com.ludens.assignment.heart.infrastructure;

import com.ludens.assignment.heart.domain.PostLike;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostLikeRepository extends JpaRepository<PostLike, Long> {

    @Modifying
    @Query("DELETE FROM PostLike pl WHERE pl.user.id = :userId AND pl.post.id = :postId")
    int deleteByUserIdAndPostId(@Param("userId") String userId, @Param("postId") Long postId);

    @Query(value = "SELECT pl FROM PostLike pl JOIN FETCH pl.user WHERE pl.post.id = :postId",
            countQuery = "SELECT COUNT(pl) FROM PostLike pl WHERE pl.post.id = :postId")
    Page<PostLike> findByPostIdWithUser(@Param("postId") Long postId, Pageable pageable);

    @Query(value = "SELECT pl FROM PostLike pl JOIN FETCH pl.post JOIN FETCH pl.post.author WHERE pl.user.id = :userId",
            countQuery = "SELECT COUNT(pl) FROM PostLike pl WHERE pl.user.id = :userId")
    Page<PostLike> findByUserIdWithPost(@Param("userId") String userId, Pageable pageable);

    long countByPostId(Long postId);

    @Query("SELECT pl.post.id, COUNT(pl) FROM PostLike pl WHERE pl.post.id IN :postIds GROUP BY pl.post.id")
    List<Object[]> countByPostIds(@Param("postIds") List<Long> postIds);

    @Query("SELECT pl.post.id FROM PostLike pl WHERE pl.user.id = :userId AND pl.post.id IN :postIds")
    List<Long> findHeartedPostIds(@Param("userId") String userId, @Param("postIds") List<Long> postIds);

    @Query("SELECT pl.post.id, COUNT(pl) FROM PostLike pl GROUP BY pl.post.id")
    List<Object[]> countAllGroupByPostId();

    boolean existsByPostIdAndPostAuthorId(Long postId, String authorId);

    boolean existsByUserIdAndPostId(String userId, Long postId);

    @Query("SELECT pl.post.id FROM PostLike pl WHERE pl.user.id = :userId")
    List<Long> findPostIdsByUserId(@Param("userId") String userId);

    @Modifying
    @Query("DELETE FROM PostLike pl WHERE pl.user.id = :userId")
    void deleteByUserId(@Param("userId") String userId);
}
