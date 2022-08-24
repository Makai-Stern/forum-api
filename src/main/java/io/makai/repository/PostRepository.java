package io.makai.repository;

import io.makai.entity.PostEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface PostRepository extends PagingAndSortingRepository<PostEntity, String> {
    @Query(value =
            "SELECT * FROM posts WHERE user_id = :userId",
            nativeQuery = true)
    List<PostEntity> findByUserId(@Param("userId") String userId, Pageable pageable);
}
