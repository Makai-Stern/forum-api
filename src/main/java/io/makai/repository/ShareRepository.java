package io.makai.repository;

import io.makai.entity.ShareEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ShareRepository extends JpaRepository<ShareEntity, String> {

    @Query(value =
            "SELECT * From shares WHERE user_id = :postId AND post_id IS NOT NULL",
            nativeQuery = true)
    List<ShareEntity> findSharedPostsByUserId(@Param("postId") String postId, Pageable pageable);

    @Query(value =
            "SELECT * From shares WHERE user_id = :userId AND comment_id IS NOT NULL",
            nativeQuery = true)
    List<ShareEntity> findSharedCommentsByUserId(@Param("userId") String userId, Pageable pageable);
}
