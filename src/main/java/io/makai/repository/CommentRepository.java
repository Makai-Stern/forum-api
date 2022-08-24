package io.makai.repository;

import io.makai.entity.CommentEntity;
import io.makai.entity.PostEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends PagingAndSortingRepository<CommentEntity, String> {

    @Query(value =
            "SELECT * FROM comments WHERE post_id = :postId",
            nativeQuery = true)
    List<PostEntity> findByPostId(@Param("postId") String postId, Pageable pageable);
}
