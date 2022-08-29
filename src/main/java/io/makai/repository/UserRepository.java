package io.makai.repository;

import io.makai.entity.UserEntity;
import io.makai.payload.dto.TopUserDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends JpaRepository<UserEntity, String> {
    UserEntity findByUsername(String username);


    @Query(value =
            "select user_id as id, username, count(*) upVoteCount\n" +
            "from upvotes JOIN users ON users.id = upvotes.user_id\n" +
//            "where upvotes.created_at > unix_timestamp(date_sub(now(), interval 1 month))\n" +
            "group by user_id\n" +
            "order by upVoteCount desc\n" +
            "limit :limit",
            nativeQuery = true)
    List<TopUserDto> getTopUsers(@Param("limit") int limit);
}
