package io.makai.repository;

import io.makai.entity.PostEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface PostRepository extends JpaRepository<PostEntity, String> {
    @Query(value =
            "SELECT * FROM posts WHERE user_id = :userId",
            nativeQuery = true)
    Page<PostEntity> findByUserId(@Param("userId") String userId, Pageable pageable);

    @Query(value =
            "SELECT * FROM posts WHERE user_id = :userId",
            nativeQuery = true)
    List<PostEntity> findByUserId(@Param("userId") String userId);

    @Query(value =
            "SELECT * FROM posts WHERE title LIKE CONCAT('%', ?1, '%')",
            nativeQuery = true)
    Page<PostEntity> search(@Param("query") String query, Pageable pageable);

    @Query(value =
            "SELECT * FROM posts WHERE title LIKE CONCAT('%', ?1, '%') ORDER BY created_at DESC",
            nativeQuery = true)
    List<PostEntity> search(@Param("query") String query);
}
