package io.makai.repository;

import io.makai.entity.DownVoteEntity;
import io.makai.entity.UpVoteEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DownVoteRepository extends JpaRepository<DownVoteEntity, String> {

    @Query(value =
            "SELECT * FROM downvotes WHERE post_id = :postId",
            nativeQuery = true)
    Page<DownVoteEntity> findByPostId(@Param("postId") String postId, Pageable pageable);


    @Query(value =
            "SELECT * FROM downvotes WHERE post_id = :postId",
            nativeQuery = true)
    List<DownVoteEntity> findByPostId(@Param("postId") String postId);

    @Query(value =
            "SELECT * FROM downvotes WHERE comment_id = :commentId",
            nativeQuery = true)
    Page<DownVoteEntity> findByCommentId(@Param("commentId") String commentId, Pageable pageable);

    @Query(value =
            "SELECT * FROM downvotes WHERE comment_id = :commentId",
            nativeQuery = true)
    List<DownVoteEntity> findByCommentId(@Param("commentId") String commentId);

    @Query(value =
            "SELECT * FROM downvotes WHERE post_id = :postId AND user_id = :userId",
            nativeQuery = true)
    DownVoteEntity findByUserIdAndPostId(@Param("userId") String userId, @Param("postId") String postId);

    @Query(value =
            "SELECT * FROM downvotes WHERE comment_id = :commentId AND user_id = :userId",
            nativeQuery = true)
    DownVoteEntity findByUserIdAndCommentId(@Param("userId") String userId, @Param("commentId") String commentId);
}
