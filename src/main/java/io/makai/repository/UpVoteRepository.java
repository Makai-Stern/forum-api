package io.makai.repository;

import io.makai.entity.UpVoteEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UpVoteRepository extends JpaRepository<UpVoteEntity, String> {

    @Query(value =
            "SELECT * FROM upvotes WHERE post_id = :postId",
            nativeQuery = true)
    Page<UpVoteEntity> findByPostId(@Param("postId") String postId, Pageable pageable);

    @Query(value =
            "SELECT * FROM upvotes WHERE post_id = :postId",
            nativeQuery = true)
    List<UpVoteEntity> findByPostId(@Param("postId") String postId);

    @Query(value =
            "SELECT * FROM upvotes WHERE comment_id = :commentId",
            nativeQuery = true)
    Page<UpVoteEntity> findByCommentId(@Param("commentId") String commentId, Pageable pageable);

    @Query(value =
            "SELECT * FROM upvotes WHERE comment_id = :commentId",
            nativeQuery = true)
    List<UpVoteEntity> findByCommentId(@Param("commentId") String commentId);

    @Query(value =
            "SELECT * FROM upvotes WHERE post_id = :postId AND user_id = :userId",
            nativeQuery = true)
    UpVoteEntity findByUserIdAndPostId(@Param("userId") String userId, @Param("postId") String postId);


    @Query(value =
            "SELECT * FROM upvotes WHERE comment_id = :commentId AND user_id = :userId",
            nativeQuery = true)
    UpVoteEntity findByUserIdAndCommentId(@Param("userId") String userId, @Param("commentId") String commentId);
}
