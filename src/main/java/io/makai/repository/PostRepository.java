package io.makai.repository;

import io.makai.entity.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface PostRepository extends JpaRepository<PostEntity, String> {
    @Query(value =
            "SELECT * FROM posts WHERE user_id = :userId",
            nativeQuery = true)
    List<PostEntity> findByUserId(@Param("userId") String userId, Pageable pageable);
}
