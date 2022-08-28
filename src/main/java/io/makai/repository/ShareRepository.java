package io.makai.repository;

import io.makai.entity.ShareEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ShareRepository extends JpaRepository<ShareEntity, String> {

    @Query(value =
            "SELECT * From shares WHERE post_id = :postId",
            nativeQuery = true)
    Page<ShareEntity> findSharedPostsByPostId(@Param("postId") String postId, Pageable pageable);


    @Query(value =
            "SELECT * From shares WHERE post_id = :postId",
            nativeQuery = true)
    List<ShareEntity> findSharedPostsByPostId(@Param("postId") String postId);

    @Query(value =
            "SELECT * From shares WHERE comment_id = :commentId",
            nativeQuery = true)
    Page<ShareEntity> findSharedCommentsByCommentId(@Param("commentId") String commentId, Pageable pageable);

    @Query(value =
            "SELECT * From shares WHERE comment_id = :commentId",
            nativeQuery = true)
    List<ShareEntity> findSharedCommentsByCommentId(@Param("commentId") String commentId);

    @Query(value =
            "SELECT * From shares WHERE user_id = :userId AND post_id = :postId",
            nativeQuery = true)
    ShareEntity findByUserIdAndPostId(@Param("userId") String userId, @Param("postId") String postId);

    @Query(value =
            "SELECT * From shares WHERE user_id = :userId AND comment_id = :commentId",
            nativeQuery = true)
    ShareEntity findByUserIdAndCommentId(@Param("userId") String userId, @Param("commentId") String commentId);

    @Query(value =
            "SELECT * From shares WHERE user_id = :userId AND comment_id IS NOT NULL",
            nativeQuery = true)
    Page<ShareEntity> findSharedCommentsByUserId(@Param("userId") String userId, Pageable pageable);

    @Query(value =
            "SELECT * From shares WHERE user_id = :userId AND comment_id IS NOT NULL",
            nativeQuery = true)
    List<ShareEntity> findSharedCommentsByUserId(@Param("userId") String userId);
}
