package io.makai.repository;

import io.makai.entity.DownVoteEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DownVoteRepository extends JpaRepository<DownVoteEntity, String> {
    @Query(value =
            "SELECT * FROM upvotes WHERE post_id = :postId",
            nativeQuery = true)
    List<DownVoteEntity> findByPostId(@Param("postId") String postId, Pageable pageable);
}
