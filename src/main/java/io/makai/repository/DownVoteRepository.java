package io.makai.repository;

import io.makai.entity.DownVoteEntity;
import io.makai.entity.PostEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DownVoteRepository extends PagingAndSortingRepository<DownVoteEntity, String> {
    @Query(value =
            "SELECT * FROM upvotes WHERE post_id = :postId",
            nativeQuery = true)
    List<DownVoteEntity> findByPostId(@Param("postId") String postId, Pageable pageable);
}
