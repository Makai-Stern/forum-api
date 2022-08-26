package io.makai.repository;

import io.makai.entity.CommentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<CommentEntity, String> {

    @Query(value =
            "SELECT * FROM comments WHERE post_id = :postId",
            nativeQuery = true)
    List<CommentEntity> findByPostId(@Param("postId") String postId);

    @Query(value =
            "SELECT * FROM comments WHERE post_id = :postId",
            nativeQuery = true)
    Page<CommentEntity> findByPostId(@Param("postId") String postId, Pageable pageable);
}
